name := """2ndYearProject"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-mailer" % "5.0.0",
  javaJdbc,
  evolutions,
  cache,
  javaWs
)
