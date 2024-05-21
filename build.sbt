name := """imdb_19052024"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "org.postgresql" % "postgresql" % "42.7.3"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.3.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.3.0"
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.6"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.26"
//libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.5.3"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
