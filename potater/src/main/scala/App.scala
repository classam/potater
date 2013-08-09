package net.lassam

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import net.lassam._

/** unfiltered plan */
class App extends unfiltered.filter.Plan {
  import QParams._

  def intent = {
    case GET(Path("/")) => {
      Html(<h1>Hello, World</h1>)
    }
    case req @ GET(Path("/users")) => {
      Auth.enticate(req)
    }
    case GET(Path(Seg("users" :: username :: Nil) ) ) if Auth.check(username) => {
      val user = PotaterUser.getUser(username)
      val list = user.settingsJson
      ResponseString( pretty(render(list)) )
    }
    case GET(Path(Seg("users" :: username :: "subscriptions" :: Nil))) if Auth.check(username) => {
      val user = PotaterUser.getUser(username)
      val list = user.subscriptionsJson
      ResponseString( pretty(render(list)) )
    }
    case GET(Path(Seg("users" :: username :: "articles" :: Nil))) if Auth.check(username) => {
      if( !Auth.check(username) ){ Redirect("/users") }
      val user = PotaterUser.getUser(username)
      val list = user.articlesJson
      ResponseString( pretty(render(list)) )
    }
    case GET(Path(Seg("users" :: username :: tail))) => {
      Unauthorized ~> ResponseString( "You are not that person!" )
    }
  }
}
