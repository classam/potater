package net.lassam

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import com.google.appengine.api.datastore.{DatastoreService, DatastoreServiceFactory}

import net.lassam._

/** unfiltered plan */
class App extends unfiltered.filter.Plan {
  import QParams._

  val JSON = new ResponseHeader("Content-Type", "application/json" :: Nil)

  def intent = {
    case GET(Path("/")) => {
      Html(<h1>Hello, World</h1>)
    }
    case req @ GET(Path("/users")) => {
      Auth.enticate(req)
    }
    case GET(Path(Seg("users" :: username :: Nil) ) ) if Auth.check(username) => {
      var datastore:DatastoreService = DatastoreServiceFactory.getDatastoreService();
      val user = User.get(username, datastore)
      ResponseString( user.entity.getKey.toString )
    }
    case GET(Path(Seg("users" :: username :: "subscriptions" :: Nil))) if Auth.check(username) => {
      //val user = PotaterUser.getUser(username)
      //val list = user.subscriptionsJson
      //ResponseString( pretty(render(list)) )
      ResponseString("Subscriptions!")
    }
    case GET(Path(Seg("users" :: username :: "articles" :: Nil))) if Auth.check(username) => {
      //val user = PotaterUser.getUser(username)
      //val list = user.articlesJson
      //ResponseString( pretty(render(list)) )
      ResponseString("Articles")
    }
    case GET(Path(Seg("users" :: username :: tail))) => {
      Unauthorized ~> ResponseString( "You are not that person!" )
    }
    case GET(Path(Seg("feeds" :: tail))) => {
      var datastore:DatastoreService = DatastoreServiceFactory.getDatastoreService();
      val url = tail.mkString("/")
      val feed = Feed.get(url, datastore)
      JSON ~> ResponseString( feed.json )
    }
    case GET(Path(Seg("process" :: tail))) => {
      var datastore:DatastoreService = DatastoreServiceFactory.getDatastoreService();
      val url = tail.mkString("/")
      var feed = Feed.get(url, datastore)
      try{
        val xml = new Fetch(url).asXML
        val syn = Syndicate.parse(xml)
        // TODO: If feed has actually been updated, call feed.updated
        // else call feed.checked
        feed = feed.updated(syn)
      }
      catch{
        // TODO: This times out really easily. I think treating it like a Task might fix that?
        case e:java.net.SocketTimeoutException => { feed = feed.broken(e.getMessage) }
        case e:java.io.IOException => { feed = feed.broken(e.getMessage) } 
        case e:Exception => { feed = feed.broken(e.getMessage) }
      }
      datastore.put(feed.entity)
      JSON ~> ResponseString( feed.json )
    }
  }
}
