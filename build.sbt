organization := "com.jumlabs"
name := "url-shortener"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.13.6"
crossScalaVersions := Seq("2.12.13", "2.13.6")

resolvers += Resolver.sonatypeRepo("snapshots")

val CatsVersion = "2.6.1"
val CirceVersion = "0.14.1"
val CirceGenericExVersion = "0.14.1"
val CirceConfigVersion = "0.8.0"
val DoobieVersion = "0.13.4"
val EnumeratumCirceVersion = "1.6.1"
val H2Version = "1.4.200"
val Http4sVersion = "0.21.24"
val KindProjectorVersion = "0.13.0"
val LogbackVersion = "1.2.3"
val Slf4jVersion = "1.7.30"
val ScalaCheckVersion = "1.15.4"
val ScalaTestVersion = "3.2.9"
val ScalaTestPlusVersion = "3.2.2.0"
val FlywayVersion = "7.9.1"
val TsecVersion = "0.2.1"
val ScalaMockVersion = "5.1.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % CatsVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-literal" % CirceVersion,
  "io.circe" %% "circe-generic-extras" % CirceGenericExVersion,
  "io.circe" %% "circe-parser" % CirceVersion,
  "io.circe" %% "circe-config" % CirceConfigVersion,
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-h2" % DoobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
  "com.beachape" %% "enumeratum-circe" % EnumeratumCirceVersion,
  "com.h2database" % "h2" % H2Version,
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "ch.qos.logback" % "logback-classic" % LogbackVersion,
  "org.flywaydb" % "flyway-core" % FlywayVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion % Test,
  "org.scalacheck" %% "scalacheck" % ScalaCheckVersion % Test,
  "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
  "org.scalatestplus" %% "scalacheck-1-14" % ScalaTestPlusVersion % Test,
  "org.scalamock" %% "scalamock" % ScalaMockVersion % Test,
  // Authentication dependencies
  "io.github.jmcardon" %% "tsec-common" % TsecVersion,
  "io.github.jmcardon" %% "tsec-password" % TsecVersion,
  "io.github.jmcardon" %% "tsec-mac" % TsecVersion,
  "io.github.jmcardon" %% "tsec-signatures" % TsecVersion,
  "io.github.jmcardon" %% "tsec-jwt-mac" % TsecVersion,
  "io.github.jmcardon" %% "tsec-jwt-sig" % TsecVersion,
  "io.github.jmcardon" %% "tsec-http4s" % TsecVersion,
)

dependencyOverrides += "org.slf4j" % "slf4j-api" % Slf4jVersion

addCompilerPlugin(
  ("org.typelevel" %% "kind-projector" % KindProjectorVersion).cross(CrossVersion.full),
)

enablePlugins(ScalafmtPlugin, JavaAppPackaging, GhpagesPlugin, MicrositesPlugin, MdocPlugin)

// Microsite settings
git.remoteRepo := "git@github.com:ottogiron/url-shortener.git"

micrositeGithubOwner := "ottogiron"

micrositeGithubRepo := "url-shortener"

micrositeName := "Url Shortener"

micrositeDescription := "An example application using FP techniques in Scala"

micrositeBaseUrl := "url-shortener"

// Note: This fixes error with sbt run not loading config properly
run / fork := true

dockerExposedPorts ++= Seq(8080)
