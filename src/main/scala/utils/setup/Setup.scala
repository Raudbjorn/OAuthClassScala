package utils.setup

import com.typesafe.config.ConfigFactory
import io.scalajs.npm.bodyparser.BodyParser
import io.scalajs.npm.express.{Application, Express}
import models.{Client, RsaKey, User}

import scala.collection.mutable

object Setup {
  private val rootConfig = ConfigFactory.defaultReference()
  private val appConfig = rootConfig.getConfig("app")
  private val serverConfig = rootConfig.getConfig("authorizationServer")
  val port: Int = serverConfig.getInt("port")
  val url: String = serverConfig.getString("url")

  val app: Application = Express()
  app.use(BodyParser.json())
  app.set("json spaces", appConfig.getInt("jsonSpaces"))

  val authorizationEndpoint = s"$url:$port/authorize"
  val tokenEndpoint = s"$url:$port/token"

  val clients: mutable.Seq[Client] = mutable.Seq(Client(
    id = "oauth-client-1",
    secret = "oauth-client-secret-1",
    redirectUris = mutable.Seq("http://localhost:9000/callback"),
    scope = "openid profile email phone address"
  ))

  val rsaKey = RsaKey(
    alg = "RS256",
    d = "ZXFizvaQ0RzWRbMExStaS_-yVnjtSQ9YslYQF1kkuIoTwFuiEQ2OywBfuyXhTvVQxIiJqPNnUyZR6kXAhyj__wS_Px1EH8zv7BHVt1N5TjJGlubt1dhAFCZQmgz0D-PfmATdf6KLL4HIijGrE8iYOPYIPF_FL8ddaxx5rsziRRnkRMX_fIHxuSQVCe401hSS3QBZOgwVdWEb1JuODT7KUk7xPpMTw5RYCeUoCYTRQ_KO8_NQMURi3GLvbgQGQgk7fmDcug3MwutmWbpe58GoSCkmExUS0U-KEkHtFiC8L6fN2jXh1whPeRCa9eoIK8nsIY05gnLKxXTn5-aPQzSy6Q",
    e = "AQAB",
    n = "p8eP5gL1H_H9UNzCuQS-vNRVz3NWxZTHYk1tG9VpkfFjWNKG3MFTNZJ1l5g_COMm2_2i_YhQNH8MJ_nQ4exKMXrWJB4tyVZohovUxfw-eLgu1XQ8oYcVYW8ym6Um-BkqwwWL6CXZ70X81YyIMrnsGTyTV6M8gBPun8g2L8KbDbXR1lDfOOWiZ2ss1CRLrmNM-GRp3Gj-ECG7_3Nx9n_s5to2ZtwJ1GS1maGjrSZ9GRAYLrHhndrL_8ie_9DS2T-ML7QNQtNkg2RvLv4f0dpjRYI23djxVtAylYK4oiT_uEMgSkc4dxwKwGuBxSO0g9JOobgfy0--FUHHYtRi0dOFZw",
    kty = "RSA",
    kid = "authserver"
  )

  val userInfo: Map[String, User] = Map(
    "alice" -> User(
      sub = "9XE3-JI34-00132A",
      preferredUsername = "alice",
      name = "Alice",
      email = "alice.wonderland@example.com",
      emailVerified = true
    ),
    "bob" -> User(
      sub = "1ZT5-OE63-57383B",
      preferredUsername = "bob",
      name = "Bob",
      email = "bob.loblob@example.net",
      emailVerified = false
    ),
    "carol" -> User(
      sub = "F5Q1-L6LGG-959FS",
      preferredUsername = "carol",
      name = "Carol",
      email = "carol.lewis@example.net",
      emailVerified = true
    )
  )

  def getUser(userName: String): Option[User] = userInfo.get(userName)

  def getClient(clientId: String): Option[Client] = clients.find(_.id == clientId)

}