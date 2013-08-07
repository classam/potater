package net.lassam

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import net.lassam._

class ArticleStub(val title: String, val sourceUrl: String, val feed: Feed, val excerpt: String) extends HasJsonObject {
  val jsonObject = ("title" -> title ) ~ ("sourceUrl" -> sourceUrl) ~ ("feed" -> feed.jsonObject) ~ ("excerpt" -> excerpt )
}
