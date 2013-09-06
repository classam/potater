package net.lassam

import net.lassam._

import com.google.appengine.api.datastore.{Entity, Text, DatastoreService, KeyFactory, Key, EntityNotFoundException}
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import java.util.{Date => OldDate}
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class Article( val feed:Feed,
               val title:String, 
               val url:String,
               val summary:String,
               val content:String,
               val guid:String,
               val updated:DateTime, 
               entity_constructor: Option[Entity] ) extends HasJsonObject {
  private var priv_entity:Option[Entity] = entity_constructor

  def this(feed:Feed, title:String, url:String, summary:String, content:String, guid:String, updated:DateTime){
    this(feed, title, url, summary, content, guid, updated, None)
  }

  def this(feed:Feed, item:SyndicateItem){
    this(feed, item.title, item.link, item.summary, item.content, item.guid, Article.defaultDateTime(item.updatedDateTime), None)
  }
  def this(feed:Feed, entity_constructor:Entity){
    this( feed, 
          entity_constructor.getProperty("title").asInstanceOf[String],
          entity_constructor.getProperty("url").asInstanceOf[String],
          entity_constructor.getProperty("summary").asInstanceOf[Text].getValue, 
          entity_constructor.getProperty("content").asInstanceOf[Text].getValue, 
          entity_constructor.getProperty("guid").asInstanceOf[String], 
          new DateTime(entity_constructor.getProperty("updated").asInstanceOf[OldDate]),
          Option.apply(entity_constructor) )
  }
  
  def jsonObject = { ("title"->title) ~ ("url"->url) ~ ("summary"->summary) ~
                     ("content"->content) ~ ("guid"->guid) ~ ("updated"->updated.toString()) ~
                     ("feed"->feed.jsonObject) }
  
  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("Article", Article.generateKey(feed, guid), feed.entity.getKey)
      ent.setProperty("guid", guid )
      ent.setProperty("updated", updated.toDate )
      ent.setUnindexedProperty("title", title)
      ent.setUnindexedProperty("url", url)
      ent.setUnindexedProperty("summary", new Text(summary))
      ent.setUnindexedProperty("content", new Text(content))
      priv_entity = Option.apply(ent)
      return priv_entity.get;
    }
  }
}

object Article {
  def generateKey( feed:Feed, guid:String ):String = {
    return feed.url + "%%" + guid
  }
  def defaultDateTime(datetime:Option[DateTime]):DateTime = datetime.isDefined match {
    case true => { datetime.get }
    case false => { new DateTime() }
  }
  def create(feed:Feed, item:SyndicateItem, datastore:DatastoreService):Article = {
    val article = new Article(feed, item) 
    datastore.put(article.entity)
    return article
  }
}
