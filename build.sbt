import org.openurp.parent.Settings.*

ThisBuild / organization := "org.openurp.std.alert"
ThisBuild / version := "0.0.3-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/std-alert"),
    "scm:git@github.com:openurp/std-alert.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "chaostone",
    name = "Tihua Duan",
    email = "duantihua@gmail.com",
    url = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "OpenURP Std Alert"
ThisBuild / homepage := Some(url("http://openurp.github.io/std-alert/index.html"))

val apiVer = "0.48.2"
val starterVer = "0.4.8"
val baseVer = "0.4.63"
val eduCoreVer = "0.4.3"

val openurp_edu_api = "org.openurp.edu" % "openurp-edu-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer
val openurp_edu_core = "org.openurp.edu" % "openurp-edu-core" % eduCoreVer

lazy val root = (project in file("."))
  .enablePlugins(WarPlugin, TomcatPlugin)
  .settings(
    name := "openurp-std-alert-webapp",
    common,
    libraryDependencies ++= Seq(openurp_stater_web, openurp_edu_core),
    libraryDependencies ++= Seq(openurp_edu_api, openurp_base_tag)
  )
