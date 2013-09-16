package net.lassam

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import com.google.appengine.api.datastore.{DatastoreService, DatastoreServiceFactory, Entity, Key}
import com.google.appengine.api.taskqueue.{Queue, QueueFactory, TaskOptions}
import com.google.appengine.api.taskqueue.TaskOptions._
import com.google.appengine.api.taskqueue.TaskOptions.Builder._

import net.lassam._

/** unfiltered plan */
class App extends unfiltered.filter.Plan {
  import QParams._

  val JSON = new ResponseHeader("Content-Type", "application/json" :: Nil)
  val datastore:DatastoreService = DatastoreServiceFactory.getDatastoreService();
  val queue:Queue = QueueFactory.getDefaultQueue();

  def queueFeed( feedUrl:String ){
    queue.add(withUrl("/process/"+feedUrl))
  }

  def intent = homeIntent.onPass(userIntent.onPass(processingIntent.onPass(subscriptionIntent.onPass(feedIntent.onPass(teapotIntent)))))

  def homeIntent = unfiltered.filter.Intent {
    case GET(Path("/")) => {
      Html(<h1>Hello, World</h1>)
    }
  }
  
  def processingIntent = unfiltered.filter.Intent {
    // GET /process : look at the top n feeds in the queue
    case GET(Path(Seg("process" :: n :: nil))) => {
      val feeds:Iterable[Feed] = Feed.getFeedsByLastCheck( n.toInt, datastore )
      JSON ~> ResponseString( HasJsonObject.listJson(feeds) )
    }
    // POST /process : load the top n feeds into Queue
    case POST(Path(Seg("process" :: n :: nil))) => {
      val feeds:Iterable[Feed] = Feed.getFeedsByLastCheck( n.toInt, datastore )
      feeds.map( (x:Feed)=>{ queueFeed(x.url) } )
      JSON ~> ResponseString( HasJsonObject.listJson(feeds) )
    }
    // GET /process/http://curtis.lassam.net/feed.xml : check this feed.
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

  def subscriptionIntent = unfiltered.filter.Intent {
    // GET /users/classam/subscriptions : display all of my subscriptions
    case GET(Path(Seg("users" :: username :: "subscriptions" :: Nil))) if Auth.check(username) => {
      JSON ~> ResponseString( HasJsonObject.listJson(Subscription.getSubscriptionsForUser(username, datastore)))
    }
    // GET /users/classam/subscriptions/http://curtis.lassam.net/feed.xml : show this subscription 
    case GET(Path(Seg("users" :: username :: "subscriptions" :: tail))) if Auth.check(username) => {
      val url = tail.mkString("/")
      var subscription = Subscription.get(username, url, datastore)
      if(!subscription.isDefined) {
        NotFound ~> ResponseString( "That subscription doesn't exist." )
      }
      JSON ~> ResponseString( subscription.get.json )
    }
    // POST /users/classam/subscriptions/http://curtis.lassam.net/feed.xml : create this subscription
    //    this will also queue up this subscription's Feed for processing. 
    case POST(Path(Seg("users" :: username :: "subscriptions" :: tail))) if Auth.check(username) => {
      val url = tail.mkString("/")
      var subscription = Subscription.get(username, url, datastore)
      if( !subscription.isDefined ){ 
        
        // Check if the Feed exists.
        var feed = Feed.get(url, datastore)
        if(!feed.isDefined) {
          feed = Option.apply( Feed.create(url, datastore) )
        }

        subscription = Option.apply( Subscription.create( username, url, datastore )) 

        queueFeed(url)
      }
      JSON ~> ResponseString( subscription.get.json )
    }

  }

  def userIntent = unfiltered.filter.Intent{
    // GET /users : log me in, or redirect me to /users/classam
    case req @ GET(Path("/users")) => {
      Auth.enticate(req)
    }
    // GET /users/classam : tell me if classam exists
    case GET(Path(Seg("users" :: username :: Nil) ) ) if Auth.check(username) => {
      val user = User.get(username, datastore)
      if( user.isDefined ){
        JSON ~> ResponseString( user.get.entity.getKey.toString )
      }
      else{
        NotFound ~> ResponseString( "User doesn't exist" )
      }
    }
    // POST /users/classam : create user classam
    case PUT(Path(Seg("users" :: username :: Nil) ) ) if Auth.check(username) => {
      var user = User.get(username, datastore)
      if( !user.isDefined ){
        user = Option.apply(User.create(username, datastore))
      }
      JSON ~> ResponseString( user.get.entity.getKey.toString )
    }
    // TODO: POST /users/classam/subscriptions/http://curtis.lassam.net/feed.xml : change the Category of this subscription
    // GET /users/classam/articles : show top 100 unread ArticleStubs for classam
    case GET(Path(Seg("users" :: username :: "articles" :: Nil))) if Auth.check(username) => {
      JSON ~> ResponseString( HasJsonObject.listJson(ArticleStub.getArticleStubsForUser(username, 100, datastore)))
    }
    // GET /users/classam/articles/http://curtis.lassam.net/feed.xml : show top unread ArticleStubs for classam in this feed.
    case GET(Path(Seg("users" :: username :: "articles" :: tail))) if Auth.check(username) => {
      val url = tail.mkString("/")
      val json = HasJsonObject.listJson( ArticleStub.getArticleStubsForSubscription( Subscription.generateKey(username, url), 1000, datastore ))
      JSON ~> ResponseString( json )
    }
    // TODO: GET /users/classam/articles/1238382932/http://curtis.lassam.net/feed.xml : show article details  
    // TODO: POST /users/classam/articles/1238382932/http://curtis.lassam.net/feed.xml : set article to "read" 
    // TODO: GET /users/classam/articles/bycategory/tech : Get ArticleStubs by subscription category

    // GET /users/classam/* : bounce unauthenticated users. 
    case GET(Path(Seg("users" :: username :: tail))) => {
      Unauthorized ~> ResponseString( "You are not that person!" )
    }
    // GET /articles/2983883293/http://curtis.lassam.net/feed.xml : show the actual article for feed at GUID.
    case GET(Path(Seg("articles" :: guid :: tail))) => {
      val url = tail.mkString("/")
      val article = Article.get( url, guid, datastore )
      if (!article.isDefined){
        NotFound ~> ResponseString( "That article doesn't exist." )
      }
      JSON ~> ResponseString( article.get.json )
    }
  }

  def feedIntent = unfiltered.filter.Intent{
    // GET /feeds/http://curtis.lassam.net/feed.xml : display feed details 
    case GET(Path(Seg("feeds" :: tail))) => {
      val url = tail.mkString("/")
      val feed = Feed.get(url, datastore)
      if( !feed.isDefined ){
        NotFound ~> ResponseString( "That feed doesn't exist." )
      }
      JSON ~> ResponseString( feed.get.json )
    }
  }

  def teapotIntent = unfiltered.filter.Intent{
    // GET /teapot : I am a teapot
    case GET(Path(Seg("teapot" :: Nil))) => {
      TeaPot ~> ResponseString( "I am a teapot." )
    }
  }
}
