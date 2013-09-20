organization := "net.lassam"

name := "Potater"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.1"

seq(appengineSettings: _*)

libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-filter" % "0.6.8",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "net.liftweb" %% "lift-json" % "2.5",
  "joda-time" % "joda-time" % "2.1",
  "org.joda" % "joda-convert" % "1.2"
  // we've commented out the unfiltered spec
  //"net.databinder" %% "unfiltered-spec" % "0.6.8" % "test",
  // uncomment the following line for persistence
  //, val jdo = "javax.jdo" % "jdo2-api" % "2.3-ea"
) ++ Seq( // local testing
  "javax.servlet" % "servlet-api" % "2.3" % "provided",
   "org.eclipse.jetty" % "jetty-webapp" % "7.4.5.v20110725" % "container"
)

resolvers ++= Seq(
 "jboss" at  "https://repository.jboss.org/nexus/content/groups/public/"
  // app engine repo, uncomment the following line for persistence resolver
  //, "nexus" at "http://maven-gae-plugin.googlecode.com/svn/repository/"
)




