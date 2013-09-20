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
  def updatedDateTime:Option[DateTime] = updated match{
    case "" => items.map(_.updatedDateTime).reduceLeft(Syndicate.laterOfTwoDateTimes);
    case _ => Syndicate.parseDate(updated);
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
  def updatedDateTime:Option[DateTime] = Syndicate.parseDate(updated)
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
    if(content.length >= 1){
      val children = content.last.child;
      if(children.filter( _.toString.trim != "" ).length > 1 ){
        return children.map(stripNamespaces).map(x => x.toString).mkString(" ").trim;
      }
      else{
        return content.last.text.trim
      }
    }
    else{
      return "";
    }
  }
  private val parsers:Array[DateTimeParser] = Array(
    ISODateTimeFormat.dateTimeParser().getParser(),
    DateTimeFormat.forPattern("E, d MMM y HH:mm:ss Z").getParser(),
    DateTimeFormat.forPattern("E, d MMM y HH:mm:ss Z '('z')'").getParser(),
    DateTimeFormat.forPattern("E, d MMM y HH:mm:ss z").getParser(),
    DateTimeFormat.forPattern("dd MMM y HH:mm:ss Z").getParser() );
  private val fmt:DateTimeFormatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
  def parseDate(s:String):Option[DateTime] = {
    try {
      Option.apply(fmt.parseDateTime(s));
    }
    catch{
      case _:Throwable => None
    }
  }
  def laterOfTwoDateTimes(x:Option[DateTime], y:Option[DateTime]):Option[DateTime] = {
    if( x.isDefined && !y.isDefined ){
      return x;
    }
    else if( y.isDefined && !x.isDefined ){
      return y;
    }
    else if( x.isDefined && y.isDefined ){
      if( x.get.compareTo(y.get) > 0 ) { return x; } else { return y; } 
    }
    else{
      return None;
    }
  }
}

class RSS2Item( var item:Node ) extends SyndicateItem{
  val title = (item \ "title").text.trim
  val description = Syndicate.html(item \ "description")
  val summary = description /* make into an excerpt */
  val content = description
  val link = (item \ "link").text.trim
  val guid = (item \ "guid").text.trim
  val updated = (item \ "pubDate").text.trim
}

class RSS2(var rss:Elem) extends Syndicate{
  val version = (rss \ "@version").text.trim
  val title = (rss \ "channel" \ "title" ).text.trim
  val link = (rss \ "channel" \ "link").text.trim
  val copyright = (rss \ "channel" \ "copyright").text.trim
  val description = (rss \ "channel" \ "description").text.trim
  val language = (rss \ "channel" \ "language").text.trim
  
  val pubdate = (rss \ "channel" \ "pubDate" ).text.trim
  val lastbuilddate = (rss \ "channel" \ "lastBuildDate" ).text.trim
  
  def pubdate_or_builddate:String = {
    val pubdateDateTime = Syndicate.parseDate( pubdate );
    val lastbuilddateDateTime = Syndicate.parseDate( lastbuilddate );
    val updatedDt = Syndicate.laterOfTwoDateTimes( pubdateDateTime, lastbuilddateDateTime )
    if (updatedDt equals pubdateDateTime){
      return pubdate;
    }
    else{
      return lastbuilddate;
    }
  }

  val updated = pubdate_or_builddate
  val items:Seq[SyndicateItem] = (rss \ "channel" \ "item").map( x => new RSS2Item( x )) 
}
  
class Atom1Item( var item:Node ) extends SyndicateItem{
  val title = (item \ "title").text.trim
  val summary = (item \ "summary").text.trim
  val content = Syndicate.html(item \ "content")
  val links = (item \ "link")
  val link = Atom1.pick_correct_link(links).trim
  val guid = (item \ "id").text.trim
  val updated = (item \ "updated").text.trim
}

class Atom1(var rss:Elem) extends Syndicate{
  val version = (rss \ "@version").text.trim
  val title = (rss \ "title" ).text.trim
  val description = Syndicate.html(rss \ "subtitle")
  val links = (rss \ "link")
  val link = Atom1.pick_correct_link(links).trim
  val copyright = (rss \ "rights").text.trim
  val language = (rss \ "language").text.trim
  val updated = (rss \ "updated" ).text.trim
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
