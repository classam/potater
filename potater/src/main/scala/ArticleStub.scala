package net.lassam

import net.lassam._

import scala.collection.JavaConverters._
import java.util.{Date => OldDate}

import com.google.appengine.api.datastore._
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class ArticleStub( val subscriptionKey:Key,
                   val articleKey:Key, 
                   val title:String, 
                   val url:String,
                   val summary:String,
                   val guid:String,
                   val updated:DateTime, 
                   val read:Boolean,
                   entity_constructor: Option[Entity] ) extends HasJsonObject {
  private var priv_entity:Option[Entity] = entity_constructor

  def this(article:Article, subscription:Subscription){
    this(subscription.entity.getKey, article.entity.getKey, article.title, article.url, article.summary, article.guid, article.updated, false, None)
  }
  def this(entity_constructor:Entity){
    this( entity_constructor.getProperty("subscription_key").asInstanceOf[Key], 
          entity_constructor.getProperty("article_key").asInstanceOf[Key],
          entity_constructor.getProperty("title").asInstanceOf[String],
          entity_constructor.getProperty("url").asInstanceOf[String],
          entity_constructor.getProperty("summary").asInstanceOf[String],
          entity_constructor.getProperty("guid").asInstanceOf[String],
          new DateTime(entity_constructor.getProperty("updated").asInstanceOf[OldDate]),
          entity_constructor.getProperty("read").asInstanceOf[Boolean],
          None )
  }
  
  def jsonObject = { ("title"->title) ~ ("url"->url) ~ ("summary"->summary) ~
                      ("subscription_key"->subscriptionKey.toString()) ~
                      ("article_key"->articleKey.toString()) ~ ("guid"->guid) ~ 
                      ("updated"->updated.toString()) ~ ("read"->read.toString()) }
  
  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("ArticleStub", ArticleStub.generateKey(subscriptionKey, guid), subscriptionKey)
      ent.setProperty("updated", updated.toDate )
      ent.setProperty("read", read )
      ent.setUnindexedProperty("guid", guid )
      ent.setUnindexedProperty("subscription_key", subscriptionKey )
      ent.setUnindexedProperty("article_key", articleKey )
      ent.setUnindexedProperty("title", title)
      ent.setUnindexedProperty("url", url)
      ent.setUnindexedProperty("summary", new Text(summary))
      priv_entity = Option.apply(ent)
      return priv_entity.get;
    }
  }
}

object ArticleStub{
  def generateKey( subscriptionKey:Key, guid:String ):Key = { 
    KeyFactory.createKey("ArticleStub", guid + "%%" + subscriptionKey.toString)
  }
  def create(article:Article, subscription:Subscription, datastore:DatastoreService):ArticleStub = {
    val articleStub = new ArticleStub(article, subscription)
    datastore.put(articleStub.entity)
    return articleStub
  }
  def getArticleStubsForUser(user:User, n_results:Integer, datastore:DatastoreService):Iterable[ArticleStub] = {
    return getArticleStubsForUser(user.entity.getKey, n_results, datastore)
  }
  def getArticleStubsForUser(username:String, n_results:Integer, datastore:DatastoreService):Iterable[ArticleStub] = {
    return getArticleStubsForUser(User.generateKey(username), n_results, datastore) 
  }
  def getArticleStubsForUser(userKey:Key, n_results:Integer, datastore:DatastoreService):Iterable[ArticleStub] = {
    val query:Query = new Query("ArticleStub").setAncestor(userKey).addSort("updated", SortDirection.ASCENDING).addFilter("read", Query.FilterOperator.EQUAL, false)
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable(FetchOptions.Builder.withLimit(n_results)).asScala.map( (entity:Entity) => new ArticleStub(entity) )
  }
  def getArticleStubsForSubscription(subscriptionKey:Key, n_results:Integer, datastore:DatastoreService):Iterable[ArticleStub] = {
    val query:Query = new Query("ArticleStub").setAncestor(subscriptionKey).addSort("updated", SortDirection.ASCENDING).addFilter("read", Query.FilterOperator.EQUAL, false)

    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable(FetchOptions.Builder.withLimit(n_results)).asScala.map( (entity:Entity) => new ArticleStub(entity) )
  }
}
