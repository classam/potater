package net.lassam

import net.lassam._

import java.util.{Date => OldDate}
import scala.collection.JavaConverters._

import com.google.appengine.api.datastore.{Entity, Text, DatastoreService, KeyFactory, Key, EntityNotFoundException, PreparedQuery}
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

// a Subcription maps a User to a Feed // 
class Subscription( val userKey:Key, val feedKey:Key, val isNew:Boolean, val unread:Integer, val category:String, entity_constructor: Option[Entity] ) extends HasJsonObject{
  private var priv_entity:Option[Entity] = entity_constructor

  def this( userk:Key, feedk:Key ){
    this( userk, feedk, true, 0, "Default", None )
  }

  def this( entity_constructor:Entity ){
    this( entity_constructor.getProperty("user_key").asInstanceOf[Key],
          entity_constructor.getProperty("feed_key").asInstanceOf[Key], 
          entity_constructor.getProperty("new").asInstanceOf[Boolean],
          entity_constructor.getProperty("unread").asInstanceOf[Integer],
          entity_constructor.getProperty("category").asInstanceOf[String],
          Option.apply(entity_constructor) )
  }

  def jsonObject = { ("category"->category) ~ ("feed"->feedKey.toString) }

  def changeCategory( newCategory:String ):Subscription = {
    entity.setUnindexedProperty("category", newCategory)
    return new Subscription( entity )
  }

  def changeUnread( unread:Integer):Subscription = {
    entity.setUnindexedProperty("unread", unread)
    if( isNew ){
      entity.setUnindexedProperty("new", false)
    }
    return new Subscription( entity )
  }

  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("Subscription", userKey.toString()+"%%"+feedKey.toString(), userKey )
      ent.setProperty("feed_key", feedKey )
      ent.setProperty("category", category )
      ent.setUnindexedProperty("user_key", userKey )
      ent.setUnindexedProperty("new", isNew )
      ent.setUnindexedProperty("unread", unread )
      priv_entity = Option.apply(ent)
      return priv_entity.get;
    }
  }
}

object Subscription {
  def generateKey(username:String, url:String):Key = { 
    generateKey( User.generateKey(username), Feed.generateKey(url)) 
  } 
  def generateKey(user_key:Key, feed_key:Key):Key = { 
    KeyFactory.createKey("Subscription", user_key.toString+"%%"+feed_key.toString)
  }
  def get(username:String, url:String, datastore:DatastoreService):Option[Subscription] = { 
    get( generateKey(username, url), datastore)
  }
  def get(user:User, feed:Feed, datastore:DatastoreService):Option[Subscription] = {
    get( generateKey(user.entity.getKey, feed.entity.getKey), datastore)
  }
  def get(subscriptionKey:Key, datastore:DatastoreService):Option[Subscription] = {
    try{
      var entity = datastore.get(subscriptionKey)
      return Option.apply(new Subscription(entity));
    }
    catch{
      case e:EntityNotFoundException => {
        return None
      }
    }
  }
  def create(username:String, url:String, datastore:DatastoreService):Subscription = {
    var subscription = new Subscription(User.generateKey(username), Feed.generateKey(url))
    datastore.put(subscription.entity)
    return subscription;
  }
  def getSubscribedTo(feed:Feed, datastore:DatastoreService):Iterable[Subscription] = {
    return getSubscribedTo(feed.entity.getKey(), datastore);
  }
  def getSubscribedTo(feedKey:Key, datastore:DatastoreService):Iterable[Subscription] = {
    val query:Query = new Query("Subscription").addFilter( "feed_key", FilterOperator.EQUAL, feedKey )
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable.asScala.map( (entity:Entity) => new Subscription(entity))
  }
  def getSubscriptionsForUser(username:String, datastore:DatastoreService):Iterable[Subscription] = {
    return getSubscriptionsForUser(KeyFactory.createKey("User", username), datastore )
  }
  def getSubscriptionsForUser(userKey:Key, datastore:DatastoreService):Iterable[Subscription] = {
    val query:Query = new Query("Subscription").setAncestor(userKey)
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable.asScala.map( (entity:Entity) => new Subscription(entity) )
  }
}
