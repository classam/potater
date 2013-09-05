package net.lassam

import net.lassam._

import com.google.appengine.api.datastore.{DatastoreService, DatastoreServiceFactory, Entity, Key, KeyFactory, EntityNotFoundException}

import com.google.appengine.api.users.{User, UserService, UserServiceFactory}


class GoogleUser ( val username: String ) extends PotaterUser{
  private var datastore:DatastoreService = DatastoreServiceFactory.getDatastoreService();

  var user:Entity = null;
  try{
    user = datastore.get(KeyFactory.createKey("User", username));
  }
  catch{
    case e:EntityNotFoundException =>{
      user = createUser(username);
    }
  }

  private def createUser(username:String):Entity = {
    val userService = UserServiceFactory.getUserService();
    val u = userService.getCurrentUser();
    if (u.getNickname() != username ){
      throw new IllegalArgumentException("You can't create a user for a nickname you don't own.");
    }
    val entity = new Entity("User", username);
    entity.setProperty("googleUser", u);
    datastore.put(entity);
    return entity;
  }

  def key:String = { user.getKey().toString() }
  def subscriptions : List[Feed] = { Nil }
  def articles : List[ArticleStub] = { Nil }
  def settings: List[UserSetting] = { Nil }
}
