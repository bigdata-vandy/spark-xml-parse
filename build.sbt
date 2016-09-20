name := "spark-xml-parse"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0",
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2",
  "net.liftweb" % "lift-json_2.10" % "2.6.3"
)