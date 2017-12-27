package utils

object UriUtils {
  import com.netaporter.uri.Uri

  def isValidUri(uri: String) = {
    val maybeUri = Uri.parse(uri)
    maybeUri.scheme.isDefined || maybeUri.host.isDefined
  }
}