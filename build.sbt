
name := "OAuthClassScala"

version := "0.1"

scalaVersion := "2.12.4"

//logLevel := sbt.Level.Debug

resolvers += Resolver.sonatypeRepo("releases")

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, ShoconPlugin)

scalacOptions += "-Ypartial-unification"

(emitSourceMaps in fullOptJS) := true

webpackConfigFile := Some(baseDirectory.value / "application.webpack.config.js")

compile in Compile := (compile in Compile).dependsOn(shoconConcat).value

scalaJSUseMainModuleInitializer := true

//JS wrappers
libraryDependencies += "io.scalajs.npm" %%% "express" % "0.4.1"
libraryDependencies += "io.scalajs.npm" %%% "body-parser" % "0.4.2"
libraryDependencies += "eu.unicredit" %%% "shocon" % "0.1.10"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.7"
libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.5.1"
libraryDependencies += "org.typelevel" %%% "cats-core" % "1.0.0-RC1"
libraryDependencies += "io.lemonlabs" %%% "scala-uri" % "0.5.0"


//NPM Dependencies
npmDependencies in Compile += "source-map-support" -> "^0.5.0"