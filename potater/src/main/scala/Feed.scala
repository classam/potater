package net.lassam

import net.lassam._

import com.google.appengine.api.datastore.{Entity, Text}
import java.util.{Date => OldDate}
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class Feed( val url: String,          // http://curtis.lassam.net/feed.xml
            val lastCheck: DateTime,  // the last time we checked the feed
            val lastUpdate: DateTime, // the last time the FEED was updated
            val status: String,       // the status: BROKEN/ACTIVE/UNKNOWN
            val title: String,        // "Curtis' Awesome Feed"
            val description: String,  // "The place where my feed is."
            entity_constructor: Option[Entity] ){

  private var priv_entity:Option[Entity] = entity_constructor;

  // Creating a new feed with nothing but a URL
  def this( url:String ){
    // the feed has never been updated or checked
    this( url, new Instant(0).toDateTime(), new Instant(0).toDateTime(), "Unknown", "Feed: "+url, "", None )
  }

  // Creating a new feed from an entity
  def this( entity_constructor:Entity ){
    this( entity_constructor.getProperty("url").asInstanceOf[String],
          new DateTime(entity_constructor.getProperty("lastCheck").asInstanceOf[OldDate]),
          new DateTime(entity_constructor.getProperty("lastUpdate").asInstanceOf[OldDate]),
          entity_constructor.getProperty("status").asInstanceOf[String],
          entity_constructor.getProperty("title").asInstanceOf[String],
          entity_constructor.getProperty("description").asInstanceOf[Text].getValue,
          Option.apply(entity_constructor) );
  }
  
  def jsonObject = {
    ("url"->url) ~ ("lastCheck"->lastCheck.toString()) ~ ("lastUpdate"->lastUpdate.toString()) ~ ("status"->status) ~ ("title"->title) ~ ("description"->description)
  }
 
  def updated(syndicateFeed:Syndicate):Feed = {
    val now = new DateTime()
    var updated:DateTime = null
    if( syndicateFeed.updatedDateTime.isDefined ){
      updated = syndicateFeed.updatedDateTime.get
    }
    else{
      updated = now
    }
    entity.setProperty( "lastCheck", now.toDate() )
    entity.setUnindexedProperty( "lastUpdate", updated.toDate() )
    entity.setUnindexedProperty( "title", syndicateFeed.title );
    entity.setUnindexedProperty( "description", new Text(syndicateFeed.description) );
    return new Feed( url, now, updated, "ACTIVE", title, description, Option.apply(entity) ); 
  }

  // Updating a feed with a new lastCheck
  def checked():Feed = {
    val now = new DateTime();
    entity.setProperty( "lastCheck", now.toDate() );
    return new Feed( url, now, lastUpdate, status, title, description, Option.apply(entity) ); 
  }

  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("Feed")
      ent.setProperty("url", url)
      ent.setProperty("lastCheck", lastCheck.toDate() )
      ent.setUnindexedProperty("lastUpdate", lastUpdate.toDate() )
      ent.setUnindexedProperty("status", status)
      ent.setUnindexedProperty("title", title)
      ent.setUnindexedProperty("description", new Text(description) )
      priv_entity = Option.apply(ent)
      return priv_entity.get
    }
  }

}
