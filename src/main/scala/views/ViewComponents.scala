package views

import scalatags.Text.TypedTag
import scalatags.Text.tags2.nav
import scalatags.generic
import scalatags.text.Builder


object ViewComponents {
  import scalatags.Text.all._
  import scalatags.Text.tags2.{style, title}

  def headComponent(headTitle: String) =  head(
    meta(charset := "utf-8"),
    meta(attr("http-equiv") := "X-UA-Compatible", content := "IE=edge"),
    meta(name := "viewport", content := "width=device-width, initial-scale=1"),
    title(headTitle),
    link(rel := "stylesheet", href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"),
    style(
      """
         body {
          padding-top: 60px;
        }
        .navbar-inverse {
          background-color: #322;
        }
      """)
  )

  def navBar(viewName: String) =  nav(cls := "navbar navbar-inverse navbar-fixed-top",
    div(cls := "container",
      div(cls := "navbar-header",
        a(cls := "navbar-brand", href := "/", "OAuth in Action:",
          span(cls := "label label-danger", viewName)
        )
      )
    )
  )

  def jumbotronContainer(content: Frag*) = div(cls := "container", div(cls := "jumbotron", content))

  object staticViews {
    val authServerHead: TypedTag[String] = ViewComponents.headComponent("OAuth in Action: OAuth Authorization Server")
    val authServerNavBar: TypedTag[String] = ViewComponents.navBar("OAuth Authorization Server")
    val bottomScripts: generic.Frag[Builder, String] = frag(
      script(src := "https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"),
      script(src := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js")
    )
  }


}
