name := "angela"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies  ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.6",
  "ch.qos.logback" %  "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
  "org.apache.jackrabbit" % "oak-jcr" % "1.5.8",
  "org.mongodb" % "casbah-core_2.9.0-1" % "3.0.0-M2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.1"
 
)
