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
class Subscription( val userKey:Key, val feedKey:Key, val category:String, entity_constructor: Option[Entity] ) extends HasJsonObject{
  private var priv_entity:Option[Entity] = entity_constructor

  def this( user:User, feed:Feed ){
    this( user.entity.getKey(), feed.entity.getKey(), "Default", None )
  }

  def this( user:User, feed:Feed, category:String ){
    this( user.entity.getKey(), feed.entity.getKey(), category, None )
  }

  def this( entity_constructor:Entity ){
    this( entity_constructor.getProperty("user_key").asInstanceOf[Key],
          entity_constructor.getProperty("feed_key").asInstanceOf[Key], 
          entity_constructor.getProperty("category").asInstanceOf[String],
          Option.apply(entity_constructor) )
  }

  def user(datastore:DatastoreService):User = {
    User.get(userKey, datastore).get;
  }

  def feed(datastore:DatastoreService):Feed = {
    Feed.get(feedKey, datastore).get;
  }

  def jsonObject = { ("category"->category) ~ ("feed"->feedKey.toString) }

  def changeCategory( newCategory:String ):Subscription = {
    entity.setUnindexedProperty("category", newCategory)
    return new Subscription( entity )
  }

  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("Subscription", Subscription.generateKey(userKey, feedKey), userKey )
      ent.setProperty("user_key", userKey )
      ent.setProperty("feed_key", feedKey )
      ent.setUnindexedProperty("category", category )
      priv_entity = Option.apply(ent)
      return priv_entity.get;
    }
  }
}

object Subscription {
  def generateKey(user_key:Key, feed_key:Key):String = { user_key.toString+"%%"+feed_key.toString }
  def get(user:User, feed:Feed, datastore:DatastoreService):Option[Subscription] = {
    try{
      var entity = datastore.get(KeyFactory.createKey("Subscription", Subscription.generateKey(user.entity.getKey, feed.entity.getKey)))
      return Option.apply(new Subscription( entity ));
    }
    catch{
      case e:EntityNotFoundException => {
        return None
      }
    }
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
  def create(user:User, feed:Feed, datastore:DatastoreService):Subscription = {
    var subscription = new Subscription(user, feed)
    datastore.put(subscription.entity)
    return subscription;
  }
  def findUsersSubscribedTo(feed:Feed, datastore:DatastoreService):Iterable[User] = {
    val query:Query = new Query("Subscription").addFilter( "feed_key", FilterOperator.EQUAL, feed.entity.getKey() )
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable.asScala.map( (entity:Entity) => new User(entity) )
  }
}
