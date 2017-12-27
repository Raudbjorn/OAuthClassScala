package utils.validation


sealed trait Err {
  def msg: String
}

case object InvalidAuthorizationRequest extends Err {
  override def msg: String = "Client Id, Redirect Uri, and Scope are required to request authorization."
}

case class UnknownClient(clientId: String) extends Err {
  override def msg: String = s"Invalid Client $clientId."
}

case object RedirectMismatch extends Err{
  override def msg: String = "Invalid redirect URI."
}

case object InvalidScope extends Err {
  override def msg: String = "Invalid scope."
}

case object MalformedUri extends Err {
  override def msg: String = "Uri is malformed."
}
