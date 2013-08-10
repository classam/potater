package net.lassam

import scala.xml._

trait Syndicate{
  def title:String
  def link:String
  def copyright:String
  def description:String
  def language:String
  def updated:String
  def items:Seq[SyndicateItem]
  override def toString:String = {
    ("Title: " + title + "\n" + 
    "Link: " + link + "\n" + 
    "Copyright: " + copyright + "\n" + 
    "Description: " + description + "\n" + 
    "Language: " + language + "\n" + 
    "Updated: " + updated + "\n" + 
    "Items: \n " + items.map(x => x.toString()))
  }
}

trait SyndicateItem{
  def title:String
  def link:String
  def summary:String
  def content:String
  def guid:String
  def updated:String
  override def toString:String = {
    ("Title: " + title + "\n" + 
    "Link: " + link + "\n" + 
    "Summary: " + summary + "\n" + 
    "Content: " + content + "\n" + 
    "Guid: " + guid + "\n" + 
    "Updated: " + updated + "\n")
  }
}

object Syndicate{
  def parse(rss:Elem):Syndicate = rss.label match{
    case "rss" => new RSS2(rss)
    case "feed" => new Atom1(rss)
    case _ => new RSS2(rss)
  }
}

class RSS2Item( var item:Node ) extends SyndicateItem{
  val title = (item \ "title").text
  val description = (item \ "description").text
  val summary = description /* make into an excerpt */
  val content = description
  val link = (item \ "link").text
  val guid = (item \ "guid").text
  val updated = (item \ "pubDate").text
}

class RSS2(var rss:Elem) extends Syndicate{
  val version = (rss \ "@version").text
  val title = (rss \ "channel" \ "title" ).text
  val link = (rss \ "channel" \ "link").text
  val copyright = (rss \ "channel" \ "copyright").text
  val description = (rss \ "channel" \ "description").text
  val language = (rss \ "channel" \ "language").text
  val pubDate = (rss \ "channel" \ "pubDate" ).text
  val lastBuildDate = (rss \ "channel" \ "lastBuildDate" ).text
  val updated = lastBuildDate /* actually I think we should take the later of pubDate/lastBuildDate */
  val items:Seq[SyndicateItem] = (rss \ "channel" \ "item").map( x => new RSS2Item( x )) 
}

class Atom1Item( var item:Node ) extends SyndicateItem{
  /* TODO: multiple link items. Pick the best */
  val title = (item \ "title").text
  val summary = (item \ "summary").text
  val content = (item \ "content").text
  val link = (item \ "link" \ "@href" ).text
  val guid = (item \ "id").text
  val updated = (item \ "updated").text
}

class Atom1(var rss:Elem) extends Syndicate{
  /* TODO: multiple link items. Pick the best */
  val version = (rss \ "@version").text
  val title = (rss \ "title" ).text
  val description = (rss \ "subtitle").text
  val link = (rss \ "link" \ "@href" ).text
  val copyright = (rss \ "rights").text
  val language = (rss \ "language").text
  val updated = (rss \ "updated" ).text
  val items:Seq[SyndicateItem] = (rss \ "entry").map( x => new Atom1Item( x )) 
}
