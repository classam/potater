package net.lassam

import net.lassam._

import scala.collection.JavaConverters._
import com.google.appengine.api.datastore._
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import java.util.{Date => OldDate}
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class Article( val feedKey:Key,
               val title:String, 
               val url:String,
               val summary:String,
               val content:String,
               val guid:String,
               val updated:DateTime, 
               entity_constructor: Option[Entity] ) extends HasJsonObject {
  private var priv_entity:Option[Entity] = entity_constructor

  def this(feedKey:Key, title:String, url:String, summary:String, content:String, guid:String, updated:DateTime){
    this(feedKey, title, url, summary, content, guid, updated, None)
  }

  def this(feedKey:Key, item:SyndicateItem){
    this(feedKey, item.title, item.link, item.summary, item.content, item.guid, Article.defaultDateTime(item.updatedDateTime), None)
  }
  def this(feedKey:Key, entity_constructor:Entity){
    this( feedKey, 
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
                     ("feedKey"->feedKey.toString()) }
  
  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
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
  def generateKey( feedKey:Key, guid:String ):Key = {
    KeyFactory.createKey("Article", feedKey.toString() + "%%" + guid)
  }
  def defaultDateTime(datetime:Option[DateTime]):DateTime = datetime.isDefined match {
    case true => { datetime.get }
    case false => { new DateTime() }
  }
  def get( feedKey:Key, guid:String):Option[Article] = {
    get( generateKey( feedKey, guid )) 
  }
  def get( articleKey:Key ):Option[Article] = {
    try{
      return Option.apply(new Article( datastore.get(key) ));
    }
    catch{
      case e:EntityNotFoundException =>{
        return None
      }
    }
  }
  def create(feedKey:Key, item:SyndicateItem, datastore:DatastoreService):Article = {
    val article = new Article(feedKey, item) 
    datastore.put(article.entity)
    return article
  }
  def getArticlesForFeed(feed:Feed, n_results:Integer, datastore:DatastoreService):Iterable[Article] = {
    return getArticlesForFeed(feed.entity.getKey, n_results, datastore)
  }
  def getArticlesForFeed(feedKey:Key, n_results:Integer, datastore:DatastoreService):Iterable[Article] = {
    val query:Query = new Query("Article").setAncestor(feedKey).addSort("updated", SortDirection.ASCENDING)
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable(FetchOptions.Builder.withLimit(n_results)).asScala.map( (entity:Entity) => new Article(feedKey, entity) )
  }
}
