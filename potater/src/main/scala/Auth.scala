package net.lassam

import com.google.appengine.api.users.{User => GoogleUser, UserService, UserServiceFactory}

import unfiltered.request._
import unfiltered.response._

object Auth{ 
  def check(username:String):Boolean = {
    val userService = UserServiceFactory.getUserService()
    val user = userService.getCurrentUser()
    if( user == null ){ return false; }
    return user.getNickname() == username
  }
  def enticate(request:HttpRequest[Any]):Redirect = {
    val userService = UserServiceFactory.getUserService()
    val user = userService.getCurrentUser()
    if(user != null){
      return Redirect("/users/"+user.getNickname())
    }
    else{
      val loginUrl = userService.createLoginURL(request.uri)
      return Redirect(loginUrl)
    }
  }
}
