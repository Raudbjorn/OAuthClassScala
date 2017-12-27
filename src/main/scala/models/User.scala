package models

case class User(sub: String, preferredUsername: String, name: String, email: String, emailVerified: Boolean)
