name := "GeoQuiz.android"

enablePlugins(AndroidApp)
enablePlugins(AndroidProtify)

allDevices in Android := true

fork in Test := true

/* fuck proguard, who cares about file sizes anyway? */
useProguard in Test := false
useProguard in Android := false
useProguard := false

// Enforce Java 7 compilation (in case you have the JDK 8 installed)
javacOptions ++=
    "-source" :: "1.7" ::
    "-target" :: "1.7" ::
    Nil


proguardVersion := "5.3.2"


libraryDependencies ++=
    aar("com.android.support" % "appcompat-v7" % "25.1.0") ::
    aar("com.android.support" % "cardview-v7" % "25.1.0") ::
    aar("com.android.support" % "design" % "25.1.0") ::
    aar("com.android.support" % "gridlayout-v7" % "25.1.0") ::
    aar("com.android.support" % "recyclerview-v7" % "25.1.0") ::
    aar("com.android.support" % "support-v4" % "25.1.0") ::
    "com.squareup.picasso" % "picasso" % "2.5.2" ::
    "com.geteit" %% "robotest" % "0.12" % "test" ::
    Nil


// Predefined as IceCreamSandwich (4.0), nothing stops you from going below
minSdkVersion := "21"

// Prevent common com.android.builder.packaging.DuplicateFileException.
// Add further file names if you experience the exception after adding new dependencies
packagingOptions := PackagingOptions(
  excludes =
    "META-INF/LICENSE" ::
      "META-INF/LICENSE.txt" ::
      "META-INF/NOTICE" ::
      "META-INF/NOTICE.txt" ::
      Nil
)

platformTarget := "android-25"
targetSdkVersion := "25"

proguardCache ++=
  "android.support" ::
    Nil



proguardOptions ++=
  "-keepattributes EnclosingMethod,InnerClasses,Signature" ::
    "-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry" ::
    "-dontwarn javax.xml.bind.DatatypeConverter" ::
    "-dontnote org.joda.time.DateTimeZone" ::
    "-dontnote scala.concurrent.stm.impl.STMImpl$" ::
    "-keep class argonaut.** { *; }" ::
    "-keep class scalaz.** { *; }" ::
    // don't warn
    "-dontwarn okio.**" ::
    "-dontwarn okhttp3.**" ::
    "-dontwarn monix.**" ::
    "-dontwarn org.jctools.**" ::
    "-dontwarn io.circe.**" ::
    // dont note
    "-dontnote okio.**" ::
    "-dontnote com.squareup.**" ::
    "-dontnote okhttp3.** " ::
    "-dontnote monix.**" ::
    "-dontnote io.circe.**" ::
    Nil




// Shortcut: allows you to execute "sbt run" instead of "sbt android:run"
run := (run in Android).evaluated

scalacOptions ++=
  // Print detailed deprecation warnings to the console
  "-deprecation" ::
    // Print detailed feature warnings to the console
    "-feature" ::
    Nil




