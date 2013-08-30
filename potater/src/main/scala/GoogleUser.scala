package net.lassam

import net.lassam._

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity

import com.google.appengine.api.users.User
import com.google.appengine.api.users.UserService
import com.google.appengine.api.users.UserServiceFactory


class GoogleUser ( val username: String ) extends PotaterUser{

  // If there is already an entity with this username, use that.
  if( false ){

  }
  // If there is no entity with this username, create one.
  else{
    val userService = UserServiceFactory.getUserService()
    val user = userService.getCurrentUser()
    if (user.getNickname() != username ){
      throw new IllegalArgumentException("You can't create a user for a nickname you don't own.");
    }
    val googleEntity = new Entity("User", username)
    googleEntity.setProperty("googleUser", user)
  }

  def subscriptions : List[Feed] = { Nil }
  def articles : List[ArticleStub] = { Nil}
  def settings: List[UserSetting] = { Nil }
}
