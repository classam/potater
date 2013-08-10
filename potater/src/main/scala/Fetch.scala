package net.lassam

import scala.xml._
import java.net.MalformedURLException
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException
import scala.collection.mutable.StringBuilder

class Fetch(val url:String) {
  /* Throws MalformedURLException and IOException */
  private val url_object = new URL(url)
  private val reader = new BufferedReader(new InputStreamReader(url_object.openStream()))
  private var builder = new StringBuilder()
  private var line = reader.readLine()
  while( line != null ){
    builder.append(line)
    line = reader.readLine() 
  }
  val output = builder.mkString( "" )

  def asXML:Elem = { XML.loadString(output) }
}
