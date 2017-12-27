package views.authorizationServer

import models.Client
import views.ViewComponents
import views.ViewComponents.staticViews._

import scalatags.Text.all._


object Index {

  def render(clients: Seq[Client], authorizationEndpoint: String, tokenEndpoint: String) =
    html(lang := "en",
      authServerHead,
      body(
        authServerNavBar,
        ViewComponents.jumbotronContainer(
          h2("Client information:"),
          clients.map(_.renderForIndex()),
          h2("Server information:"),
          li(b("authorization_endpoint:"), code(authorizationEndpoint)),
          li(b("token_endpoint"), code(tokenEndpoint))
        )
      ),
      bottomScripts
    ).render

}
