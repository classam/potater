package net.lassam

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import com.google.appengine.api.datastore.{DatastoreService, DatastoreServiceFactory, Entity, Key}

import net.lassam._

/** unfiltered plan */
class App extends unfiltered.filter.Plan {
  import QParams._

  val JSON = new ResponseHeader("Content-Type", "application/json" :: Nil)
  val datastore:DatastoreService = DatastoreServiceFactory.getDatastoreService();

  def intent = {
    case GET(Path("/")) => {
      Html(<h1>Hello, World</h1>)
    }
    case req @ GET(Path("/users")) => {
      Auth.enticate(req)
    }
    case GET(Path(Seg("users" :: username :: Nil) ) ) if Auth.check(username) => {
      val user = User.get(username, datastore)
      if( user.isDefined ){
        ResponseString( user.get.entity.getKey.toString )
      }
      else{
        ResponseString( "User "+username+" doesn't exist" )
      }
    }
    case PUT(Path(Seg("users" :: username :: Nil) ) ) if Auth.check(username) => {
      var user = User.get(username, datastore)
      if( !user.isDefined ){
        user = Option.apply(User.create(username, datastore))
      }
      ResponseString( user.get.entity.getKey.toString )
    }
    case GET(Path(Seg("users" :: username :: "subscriptions" :: Nil))) if Auth.check(username) => {
      ResponseString("Subscriptions!")
    }
    case GET(Path(Seg("users" :: username :: "articles" :: Nil))) if Auth.check(username) => {
      ResponseString( HasJsonObject.listJson(ArticleStub.getArticleStubsForUser(username, 100, datastore)))
    }
    case GET(Path(Seg("users" :: username :: tail))) => {
      Unauthorized ~> ResponseString( "You are not that person!" )
    }
    case GET(Path(Seg("feeds" :: tail))) => {
      val url = tail.mkString("/")
      val feed = Feed.get(url, datastore).get
      JSON ~> ResponseString( feed.json )
    }
    case POST(Path(Seg("process" :: tail))) => {
      val url = tail.mkString("/")
      
      // Get Feed
      var maybeFeed = Feed.get(url, datastore)
      if (!maybeFeed.isDefined){
        ResponseString( "That feed doesn't exist." )
      }
      var feed = maybeFeed.get

      // Update Feed
      var syn:Syndicate = null
      try{
        val xml = new Fetch(url).asXML
        syn = Syndicate.parse(xml)
        feed = feed.updated(syn)
      }
      catch{
        // TODO: This times out really easily. I think treating it like a Task might fix that?
        case e:java.net.SocketTimeoutException => { feed = feed.broken(e.getMessage) }
        case e:java.io.IOException => { feed = feed.broken(e.getMessage) } 
        case e:Exception => { feed = feed.broken(e.getMessage) }
      }
      datastore.put(feed.entity)

      // Which of these articles are new?
      val oldArticles = Article.getArticlesForFeed(feed, 10, datastore)
      def alreadyExists(syndicateItem:SyndicateItem):Boolean = {
        oldArticles.exists(_.guid == syndicateItem.guid) 
      }
        // create Article objects for the new articles. 
      val newArticles = syn.items.filter( !alreadyExists(_) ).map(new Article(feed.entity.getKey, _))
     
      // create ArticleStubs for new articles
      var subscriptions = Subscription.getSubscribedTo( feed, datastore ) 
      for(subscription <- subscriptions ){
        var addedArticles = 0
        if( subscription.isNew ){
          oldArticles.map( ArticleStub.create(_, subscription, datastore ) )
          addedArticles = oldArticles.size
        }
        newArticles.map( ArticleStub.create(_, subscription, datastore ) )
        addedArticles = addedArticles + newArticles.size
        if( addedArticles > 0 ){
          datastore.put( subscription.changeUnread(subscription.unread + addedArticles).entity )
        }
      }
      
      JSON ~> ResponseString( feed.json )
    }
  }
}
