package net.lassam

import net.lassam._

import scala.collection.JavaConverters._
import java.util.{Date => OldDate}

import com.google.appengine.api.datastore.{Entity, Text, DatastoreService, KeyFactory, Key, EntityNotFoundException, PreparedQuery}
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class ArticleStub( val subscriptionKey:Key,
                   val title:String, 
                   val url:String,
                   val summary:String,
                   val guid:String,
                   val updated:DateTime, 
                   entity_constructor: Option[Entity] ) extends HasJsonObject {
  private var priv_entity:Option[Entity] = entity_constructor

  def this(article:Article, subscription:Subscription){
    this(subscription.entity.getKey, article.title, article.url, article.summary, article.guid, article.updated, None)
  }
  def this(entity_constructor:Entity){
    this( entity_constructor.getProperty("subscription_key").asInstanceOf[Key], 
          entity_constructor.getProperty("title").asInstanceOf[String],
          entity_constructor.getProperty("url").asInstanceOf[String],
          entity_constructor.getProperty("summary").asInstanceOf[String],
          entity_constructor.getProperty("guid").asInstanceOf[String],
          new DateTime(entity_constructor.getProperty("updated").asInstanceOf[OldDate]),
          None )
  }

  def subscription(datastore:DatastoreService){
    Subscription.get(subscriptionKey, datastore)
  }
  
  def jsonObject = { ("title"->title) ~ ("url"->url) ~ ("summary"->summary) ~
                      ("subscription_key"->subscriptionKey.toString()) ~ ("guid"->guid) ~ 
                      ("updated"->updated.toString()) }
  
  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("ArticleStub", ArticleStub.generateKey(subscriptionKey, guid), subscriptionKey)
      ent.setProperty("guid", guid )
      ent.setProperty("updated", updated.toDate )
      ent.setUnindexedProperty("subscription_key", subscriptionKey )
      ent.setUnindexedProperty("title", title)
      ent.setUnindexedProperty("url", url)
      ent.setUnindexedProperty("summary", new Text(summary))
      priv_entity = Option.apply(ent)
      return priv_entity.get;
    }
  }
}

object ArticleStub{
  def generateKey( subscriptionKey:Key, guid:String ):String = { 
    guid + "%%" + subscriptionKey.toString
  }
  def create(article:Article, subscription:Subscription, datastore:DatastoreService):ArticleStub = {
    val articleStub = new ArticleStub(article, subscription)
    datastore.put(articleStub.entity)
    return articleStub
  }
  def getArticleStubsForUser(user:User, datastore:DatastoreService):Iterable[ArticleStub] = {
    val query:Query = new Query("ArticleStub").setAncestor(user.entity.getKey)
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable.asScala.map( (entity:Entity) => new ArticleStub(entity) )
  }
  def getArticleStubsForSubscription(subscription:Subscription, datastore:DatastoreService):Iterable[ArticleStub] = {
    val query:Query = new Query("ArticleStub").setAncestor(subscription.entity.getKey)
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable.asScala.map( (entity:Entity) => new ArticleStub(entity) )
  }
}
