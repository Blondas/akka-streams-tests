name := "akka-streams-tests"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-stream_2.12" % "2.5.2",
  "com.typesafe.akka" % "akka-testkit_2.12" % "2.5.2" % "test",
  "org.scalatest" % "scalatest_2.12" % "3.0.3" % "test"
)
        