name := "CDR-Generator"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.0.0"

libraryDependencies += "org.apache.spark" %% "spark-graphx" % "1.0.0"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.2.0"

libraryDependencies += "joda-time" % "joda-time" % "2.3"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/NOTE.md")

scalacOptions in (Compile, doc) ++= Seq("-doc-title", "CDR Generator")

resolvers += "Akka Repository" at "http://repo.akka.io/release/"

