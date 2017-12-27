
import cats.data.Validated.{Invalid, Valid}
import io.scalajs.npm.express.{Request, Response}
import views.authorizationServer.Index
import wrappers.Authorization._

import scala.collection.mutable

object AuthorizationServer extends App {
  import utils.setup.Setup._

  val requestStore = mutable.Map[String, ValidatedAuthorizationRequest]()

  app.get("/", (_: Request, res: Response) => res.send(Index.render(clients, authorizationEndpoint, tokenEndpoint)))

  app.get("/authorize", (req: Request, res: Response) => req.validate match {
    case (Invalid(query), _) => res.renderErrorPage(query)
    case (Valid(query), Invalid(request)) if query.needsRedirect(request) => res.redirectToErrorPage(query.redirectUri, request)
    case (_, Invalid(request))  => res.renderErrorPage(request)
    case (Valid(_), Valid(request)) =>
      requestStore.put(request.id, request)
      res.renderApprovalPage(request)
  })


  app.listen(port)
}


