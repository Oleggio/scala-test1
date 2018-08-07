name := "scala-test1"
organization := "template"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-stream" % "2.5.14",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.3",
  "com.h2database" % "h2" % "1.4.197",
  "org.json4s" %% "json4s-jackson" % "3.6.0",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.softwaremill.sttp" %% "core" % "1.3.0-RC5" % Test
)

enablePlugins(JavaAppPackaging)

dockerBaseImage := "anapsix/alpine-java"
dockerExposedPorts ++= Seq(8080)