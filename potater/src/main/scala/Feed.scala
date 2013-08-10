package net.lassam

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class Feed( val title: String, val url: String, val description: String ) extends HasJsonObject{
  val jsonObject = ("title"-> title) ~ ("url"-> url) ~ ("description"-> description)
}
