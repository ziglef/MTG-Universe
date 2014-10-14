name := """ldso-turma-1-2014-2015"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.4.3",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.0"
)