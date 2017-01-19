
lazy val commonSettings = Seq(
  name := "GeoQuiz",
  organization := "com.kerbel",
  version := "0.1.0",
  versionCode := Some(1),
  scalaVersion := "2.11.8",
  libraryDependencies := commonLibs
  )

lazy val core = (project in file("core")).settings(commonSettings: _*)
lazy val android = (project in file("android"))
                                               .dependsOn(core)
											   .settings(commonSettings: _*)


lazy val commonLibs =    
    "org.scalaz" %% "scalaz-core" % "7.2.8" ::
    "io.argonaut" %% "argonaut" % "6.1" ::
    "io.monix" %% "monix" % "2.1.2" ::
    "io.monix" %% "monix-scalaz-72" % "2.1.2" ::
    "com.squareup.okhttp3" % "okhttp" % "3.5.0" ::
    "org.scalatest" %% "scalatest" % "3.0.1" % "test" ::
    "io.taig" %% "communicator" % "3.0.0" ::
    Nil