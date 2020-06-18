name := "akkaApplication"

version := "1.0"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.13",
  "org.scalatest" %% "scalatest" % "3.0.5"
)




