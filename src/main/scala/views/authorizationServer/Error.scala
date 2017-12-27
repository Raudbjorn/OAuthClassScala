package views.authorizationServer


import utils.validation.Err
import views.ViewComponents
import views.ViewComponents.staticViews._

import scalatags.Text.all._

object Error {

  def render(errors: Seq[Err]) = html(lang := "en",
    authServerHead,
    body(
      authServerNavBar,
      ViewComponents.jumbotronContainer(
        h2(cls := "text-danger", "Error"),
          ul(
            for(error <- errors) yield li(error.msg)
          )
      ),
      bottomScripts
    )
  ).render

}
