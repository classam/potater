package net.lassam

import net.lassam._

import com.google.appengine.api.datastore._
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import java.util.{Date => OldDate}
import scala.collection.JavaConverters._
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class Feed( val url: String,                    // http://curtis.lassam.net/feed.xml
            val lastCheck: DateTime,            // the last time we checked the feed
            val lastUpdate: DateTime,           // the last time the FEED was updated
            val status: String,                 // the status: Broken/Active/Unknown
            val title: String,                  // "Curtis' Awesome Feed"
            val description: String,            // "The place where my feed is."
            val error: String,                  // If the feed's broken, this is why
            entity_constructor: Option[Entity]  /* pass in if this Entity already exists */ ) extends HasJsonObject{

  private var priv_entity:Option[Entity] = entity_constructor

  // Creating a new feed with nothing but a URL
  def this( url:String ){
    // the feed has never been updated or checked
    this( url, new Instant(0).toDateTime(), new Instant(0).toDateTime(), "Unknown", "Feed: "+url, "", "",  None )
  }

  // Creating a new feed from an entity
  def this( entity_constructor:Entity ){
    this( entity_constructor.getProperty("url").asInstanceOf[String],
          new DateTime(entity_constructor.getProperty("lastCheck").asInstanceOf[OldDate]),
          new DateTime(entity_constructor.getProperty("lastUpdate").asInstanceOf[OldDate]),
          entity_constructor.getProperty("status").asInstanceOf[String],
          entity_constructor.getProperty("title").asInstanceOf[String],
          entity_constructor.getProperty("description").asInstanceOf[Text].getValue,
          entity_constructor.getProperty("error").asInstanceOf[String],
          Option.apply(entity_constructor) )
  }
  
  def jsonObject = {
    ("url"->url) ~ ("lastCheck"->lastCheck.toString()) ~ ("lastUpdate"->lastUpdate.toString()) ~ ("status"->status) ~ ("title"->title) ~
    ("description"->description) ~ ("error"->error)
  }

  // Checked, valid response
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
    entity.setUnindexedProperty( "status", "Active" )
    entity.setUnindexedProperty( "title", syndicateFeed.title )
    entity.setUnindexedProperty( "description", new Text(syndicateFeed.description) )
    entity.setUnindexedProperty( "error", "" );
    return new Feed( entity ) 
  }

  // Checked, no response
  def broken(error:String):Feed = {
    entity.setUnindexedProperty("status", "Broken")
    entity.setUnindexedProperty("error", error)
    return new Feed( entity ) 
  }

  // Checked, no change. 
  def checked():Feed = {
    val now = new DateTime()
    entity.setProperty( "lastCheck", now.toDate() )
    return new Feed( entity )
  }

  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent:Entity = new Entity("Feed", url)
      ent.setProperty("lastCheck", lastCheck.toDate() )
      ent.setUnindexedProperty("url", url)
      ent.setUnindexedProperty("lastUpdate", lastUpdate.toDate() )
      ent.setUnindexedProperty("status", status)
      ent.setUnindexedProperty("title", title)
      ent.setUnindexedProperty("description", new Text(description) )
      ent.setUnindexedProperty("error", "")
      priv_entity = Option.apply(ent)
      return priv_entity.get
    }
  }
}

object Feed {
  def generateKey(url:String):Key = { KeyFactory.createKey("Feed", url)  }
  def get(url:String, datastore:DatastoreService):Option[Feed] = {
    Feed.get(generateKey(url), datastore)
  }
  def get(key:Key, datastore:DatastoreService):Option[Feed] = {
    try{
      return Option.apply(new Feed( datastore.get(key) ));
    }
    catch{
      case e:EntityNotFoundException =>{
        return None
      }
    }
  }
  def getFeedsByLastCheck(n_results:Integer, datastore:DatastoreService):Iterable[Feed] = {
    val query:Query = new Query("Feed").addSort("lastCheck", SortDirection.DESCENDING)
    val result:PreparedQuery = datastore.prepare(query)
    return result.asIterable(FetchOptions.Builder.withLimit(n_results)).asScala.map( (entity:Entity) => new Feed(entity) )
  }
  def create(url:String, datastore:DatastoreService):Feed = {
    var feed = new Feed(url)
    datastore.put(feed.entity)
    return feed
  }

}
