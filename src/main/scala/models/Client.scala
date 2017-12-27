package models

import scalatags.Text.all._

case class Client(id: String, secret: String, redirectUris: Seq[String], scope: String,
                  name: Option[String] = None, uri: Option[String] = None, logoUri: Option[String] = None){
  def renderForIndex() = ul(
    li(b("client_id: "), code(id)),
    li(b("client_secret: "), code(secret)),
    li(b("scope: "), code(scope)),
    li(b("redirect_uris: "), code(redirectUris.mkString(",")))
  )

  def renderForApprove() = frag(
    name.map(n => p(b("Name:"), code(n))).getOrElse(UnitFrag(Unit)),
    p(b("ID:"), code(id)),
    uri.map(u => p(b("URI:"), code(uri))).getOrElse(UnitFrag(Unit)),
    logoUri.map(l => p(b("logo:", img(src := l)))).getOrElse(UnitFrag(Unit))
  )
}

