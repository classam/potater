package net.lassam

import scala.xml._
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException
import scala.collection.mutable.StringBuilder

class Fetch(val url:String) {
  /* Throws MalformedURLException and IOException */
  private val urlObject = new URL(url)
  private val urlConnection = urlObject.openConnection();
  urlConnection.setConnectTimeout(5000)
  urlConnection.setReadTimeout(5000)
  private val reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))
  private var builder = new StringBuilder()
  private var line = reader.readLine()
  while( line != null ){
    builder.append(line)
    line = reader.readLine() 
  }
  val output = builder.mkString( "" )

  def asXML:Elem = { XML.loadString(output) }
}
