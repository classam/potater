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

class Article( ent:Option[Entity] ) extends EntityWrapper(ent) {

  def this(){ this(None) } 

  def properties:List[Property] = {
    IndexedStringProperty("guid") ::
    IndexedDateTimeProperty("updated") ::
    StringProperty("title") ::
    StringProperty("url") ::
    TextProperty("summary") ::
    TextProperty("content") :: 
    KeyProperty("feedkey") :: 
    Nil
  }
  def entityName:String = "Article"
  def keyString:String = { Article.generateKeyString( self.feedkey, self.guid ) }
  def parentKey:Option[Key] = None
 
  def importSyndicate(item:SyndicateItem){
    self.title = item.title
    self.link = item.link
    self.summary = item.summary
    self.content = item.content
    self.guid = item.guid
    self.updated = Article.defaultDateTime( item.updated )
  }

}

object Article {
  def generateKeyString( feedKey:Key, guid:String ):String = {
    return feedKey.toString() + "%%" + guid
  }
  def generateKey( feedKey:Key, guid:String ):Key = {
    KeyFactory.createKey(generateKeyString(feedKey, guid))
  }
  def defaultDateTime(datetime:Option[DateTime]):DateTime = datetime.isDefined match {
    case true => { datetime.get }
    case false => { new DateTime() }
  }
  def get( url:String, guid:String, datastore:DatastoreService):Option[Article] = {
    get( generateKey( Feed.generateKey(url), guid ), datastore) 
  }
  def get( articleKey:Key, datastore:DatastoreService ):Option[Article] = {
    try{
      return Option.apply(new Article( datastore.get(articleKey) ));
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
    return result.asIterable(FetchOptions.Builder.withLimit(n_results)).asScala.map( (entity:Entity) => new Article(entity) )
  }
}
