package views.authorizationServer

import models.Client
import views.ViewComponents
import views.ViewComponents.staticViews._
import utils.setup.Setup.userInfo

import scalatags.Text.all._

object Approve {

  def render(client: Client, requestId: String, scope: String) = html(lang := "en",
    authServerHead,
    body(
      authServerNavBar,
      ViewComponents.jumbotronContainer(
        h2("Approve this client?"),
        client.renderForApprove(),
        form(cls := "form", action := "/approve", method := "POST",
          label("Select user:"),
          select(name := "user",
            for(user <- userInfo.values.toSeq) yield option(value := user.preferredUsername, user.name)
          ),
          input(`type` := "hidden", name := "reqid", value := requestId),
          if(scope.nonEmpty) {
            frag(
              p("The client is requesting access to the following:"),
              ul(
                scope.split(" ").toSeq.map(s =>
                  li(
                  input(`type` := "checkbox", name := s"scope_$s", id := s"scope_$s", checked := "checked"),
                  label(`for` := s"scope_$s", s))))
              )
          } else UnitFrag(Unit),
          input(`type` := "submit", cls := "btn btn-success", name := "approve", value := "Approve"),
          input(`type` := "submit", cls := "btn btn-danger", name := "deny", value := "Deny")
        )
      ),
      bottomScripts
    )
  ).render

}
