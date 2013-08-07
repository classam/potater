package net.lassam

import unfiltered.request._
import unfiltered.response._

import com.google.appengine.api.users.UserService
import com.google.appengine.api.users.UserServiceFactory

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
    case GET(Path("/users")) => {
      /* get username, redirect to /users/username */
      Html(<h1>Oh YEAH</h1>)
    }
    case GET(Path(Seg("users" :: username :: Nil) ) ) => {
      val user = PotaterUser.getUser(username)
      val list = user.settingsJson
      ResponseString( pretty(render(list)) )
    }
    case GET(Path(Seg("users" :: username :: "subscriptions" :: Nil))) => {
      val user = PotaterUser.getUser(username)
      val list = user.subscriptionsJson
      ResponseString( pretty(render(list)) )
    }
    case GET(Path(Seg("users" :: username :: "articles" :: Nil))) => {
      val user = PotaterUser.getUser(username)
      val list = user.articlesJson
      ResponseString( pretty(render(list)) )
    }
  }
}
