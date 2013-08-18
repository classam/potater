package net.lassam

import scala.xml._
import org.joda.time.format._
import org.joda.time._

trait Syndicate{
  def title:String
  def link:String
  def copyright:String
  def description:String
  def language:String
  def updated:String
  def items:Seq[SyndicateItem]
  def updatedDateTime:DateTime = {
    if( updated != "" ){
      return Syndicate.fmt.parseDateTime(updated);
    }
    else {
      val datetimes = items.map( _.updatedDateTime );
      return datetimes.reduceLeft( (x:DateTime, y:DateTime) => { if( x.compareTo(y) > 0 ) { return x; } else { return y; } } );
    }
  }
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
  def updatedDateTime:DateTime = {
    return Syndicate.fmt.parseDateTime(updated);
  }
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
  def html(content:NodeSeq):String = {
    def stripNamespaces(node:Node):Node = {
      node match {
        case e : Elem =>
          e.copy(scope = TopScope, child=e.child.map(stripNamespaces));
        case _ => node;
      }
    }
    content.last.child.map(stripNamespaces).map(x => x.toString).mkString(" ")
  }
  val parsers:Array[DateTimeParser] = Array(
    ISODateTimeFormat.dateTimeParser().getParser(),
    DateTimeFormat.forPattern("E, d MMM y HH:mm:ss Z").getParser(),
    DateTimeFormat.forPattern("E, d MMM y HH:mm:ss Z '('z')'").getParser(),
    DateTimeFormat.forPattern("E, d MMM y HH:mm:ss z").getParser(),
    DateTimeFormat.forPattern("dd MMM y HH:mm:ss Z").getParser() );
  val fmt:DateTimeFormatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
}

class RSS2Item( var item:Node ) extends SyndicateItem{
  val title = (item \ "title").text
  val description = Syndicate.html(item \ "description")
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
  val title = (item \ "title").text
  val summary = (item \ "summary").text
  val content = (item \ "content").text
  val links = (item \ "link")
  val link = Atom1.pick_correct_link(links)
  val guid = (item \ "id").text
  val updated = (item \ "updated").text
}

class Atom1(var rss:Elem) extends Syndicate{
  val version = (rss \ "@version").text
  val title = (rss \ "title" ).text
  val description = (rss \ "subtitle").text
  val links = (rss \ "link")
  val link = Atom1.pick_correct_link(links)
  val copyright = (rss \ "rights").text
  val language = (rss \ "language").text
  val updated = (rss \ "updated" ).text
  val items:Seq[SyndicateItem] = (rss \ "entry").map( x => new Atom1Item( x )) 
}

object Atom1 {
  def pick_correct_link(links:NodeSeq):String = {
    val links_with_no_rel = links.filter(x => x.attribute("rel") == None)
    if( links_with_no_rel.length > 0 ){
      return (links_with_no_rel.last \ "@href" ).toString
    }
    else if( links.length > 0 ){
      return (links.last \ "@href").toString
    }
    else{
      return ""
    }
  }
}
