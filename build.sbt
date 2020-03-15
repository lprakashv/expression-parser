name := "expression-parser"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "fastparse" % "2.1.3",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

coverageMinimum := 50
coverageFailOnMinimum := true
coverageEnabled := true
coverageExcludedPackages := "com.lprakashv.Repl.*;"
