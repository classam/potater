package net.lassam

import net.lassam._

import com.google.appengine.api.datastore.{Entity, Text, DatastoreService, KeyFactory, Key, EntityNotFoundException}
import java.util.{Date => OldDate}
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

// a Subcription maps a User to a Feed // 
class Subscription( val user:User, val feed:Feed, val category:String, entity_constructor: Option[Entity] ) extends HasJsonObject{
  private var priv_entity:Option[Entity] = entity_constructor

  def this( user:User, feed:Feed){
    this( user, feed, "Default", None )
  }

  def this( user:User, feed:Feed, category:String ){
    this( user, feed, category, None )
  }

  def this( entity_constructor:Entity, datastore:DatastoreService ){
    this( User.get(entity_constructor.getProperty("user_key").asInstanceOf[Key], datastore),
          Feed.get(entity_constructor.getProperty("feed_key").asInstanceOf[Key], datastore), 
          entity_constructor.getProperty("category").asInstanceOf[String],
          Option.apply(entity_constructor) )
  }
  
  def jsonObject = { ("category"->category) ~ ("user"->user.jsonObject) ~ ("feed"->feed.jsonObject) }

  def changeCategory( newCategory:String ):Subscription = {
    entity.setUnindexedProperty("category", newCategory)
    return new Subscription( user, feed, newCategory, Option.apply(entity) )
  }

  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("Subscription", Subscription.generateKey(user, feed), user.entity.getKey )
      ent.setProperty("user_key", user.entity.getKey )
      ent.setProperty("feed_key", feed.entity.getKey )
      ent.setUnindexedProperty("username", user.username )
      ent.setUnindexedProperty("feed", feed.url )
      priv_entity = Option.apply(ent)
      return priv_entity.get;
    }
  }
}

object Subscription {
  def generateKey( user:User, feed:Feed ):String = { user.username+"%%"+feed.url }
  def get(user:User, feed:Feed, datastore:DatastoreService):Subscription = {
    try{
      var entity = datastore.get(KeyFactory.createKey("Subscription", Subscription.generateKey(user,feed)))
      return new Subscription( entity, datastore );
    }
    catch{
      case e:EntityNotFoundException => {
        var subscription = new Subscription(user, feed)
        datastore.put(subscription.entity)
        return subscription;
      }
    }
  }
}
