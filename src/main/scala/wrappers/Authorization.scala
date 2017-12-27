package wrappers

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, ValidatedNel}
import cats.implicits._
import io.scalajs.npm.express.{Request, Response}
import models.Client
import utils.UriUtils._
import utils.setup.Setup.getClient
import utils.validation._

import scala.scalajs.js
import scala.util.Random

object Authorization {

  final case class ValidatedAuthorizationRequest(client: Client, redirectUri: String, scope: String){
    val id: String = Random.nextString(8)
  }

  final case class AuthorizationRequestQuery(clientId: String, redirectUri: String, scope: String){
    def needsRedirect(errors: NonEmptyList[Err]) = !(errors.exists(_ == RedirectMismatch) || errors.exists(_ == MalformedUri)) && errors.exists(_ == InvalidScope)
  }

  implicit class AuthorizationRequest(request: Request) {
    import AuthorizationRequest._
    private val incomingQuery = request.queryAs[IncomingAuthorizationRequestQuery]

     private val validateQuery = validateQueryContents(incomingQuery)

    private val validatedRequest = validateQuery match {
      case errors @ Invalid(_) => errors
      case Valid(query) => validateAuthorizationRequestQuery(query)
    }

    val validate: (ValidatedNel[Err, AuthorizationRequestQuery], ValidatedNel[Err, ValidatedAuthorizationRequest]) = (validateQuery,  validatedRequest)
  }

  object AuthorizationRequest {

    private def validateClient(clientId: String): ValidatedNel[Err, Client] = getClient(clientId) match {
      case Some(client) => client.validNel
      case None =>
        println(s"Unknown client $clientId")
        UnknownClient(clientId).invalidNel
    }

    private def validateRedirectUri(client: Client, uri: String): ValidatedNel[Err, String] =
      if (!isValidUri(uri)) {
        println(s"Detected malformed uri: $uri")
        MalformedUri.invalidNel
      } else if (!client.redirectUris.contains(uri)) {
        println(s"Mismatched redirect URI, expected ${client.redirectUris.mkString(",")}, got $uri")
        RedirectMismatch.invalidNel
      } else uri.validNel

    private def validateScope(client: Client, scope: String): ValidatedNel[Err, String] = {
      val clientScope = client.scope.split(" ")
      val requestScope = scope.split(" ")
      if (requestScope.forall(s => clientScope.contains(s))) scope.validNel else InvalidScope.invalidNel
    }

    private def validateQueryContents(query: IncomingAuthorizationRequestQuery):  ValidatedNel[Err, AuthorizationRequestQuery] =
      (for {
        cId <- query.clientId.toOption
        rUri <- query.redirectUri.toOption
        s <- query.scope.toOption
      } yield AuthorizationRequestQuery(cId, rUri, s)) match {
        case Some(success) => success.validNel
        case None => InvalidAuthorizationRequest.invalidNel
      }

    def validateAuthorizationRequestQuery(authQuery: AuthorizationRequestQuery): ValidatedNel[Err, ValidatedAuthorizationRequest] =
      validateClient(authQuery.clientId) match {
        case error @ Invalid(_) => error
        case Valid(client) => (client.validNel, validateRedirectUri(client, authQuery.redirectUri), validateScope(client, authQuery.scope)).mapN(ValidatedAuthorizationRequest)
      }
  }


  implicit class AuthorizationResponse(response: Response) {
    import com.netaporter.uri.dsl._

    def renderErrorPage(errors: NonEmptyList[Err]) = response.send(views.authorizationServer.Error.render(errors.toList))

    def redirectToErrorPage(uri: String, errors: NonEmptyList[Err]) = response.redirect(uri.addParams(errors.toList.map(e => e.getClass.getSimpleName -> e.msg)))

    def renderApprovalPage(request: ValidatedAuthorizationRequest) = response.send(views.authorizationServer.Approve.render(request.client, request.id, request.scope))
  }


  @js.native
  trait IncomingAuthorizationRequestQuery extends js.Object {
    var clientId: js.UndefOr[String] = js.native
    var redirectUri: js.UndefOr[String] = js.native
    var scope: js.UndefOr[String] = js.native
  }

}
