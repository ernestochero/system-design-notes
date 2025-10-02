ThisBuild / scalaVersion := "3.3.1"

lazy val scalaExamples = (project in file("."))
  .settings(
    name := "system-design-scala-examples",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    )
  )
