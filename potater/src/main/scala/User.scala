package net.lassam

import net.lassam._

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import com.google.appengine.api.datastore.{DatastoreService, Entity, Key, KeyFactory, EntityNotFoundException}

import com.google.appengine.api.users.{User => GoogleUser, UserService, UserServiceFactory}

class User( val username:String, entity_constructor:Option[Entity] ) extends HasJsonObject{

  private var priv_entity:Option[Entity] = entity_constructor;
  val googleUser = getGoogleUser()
  if (googleUser == null || googleUser.getNickname() != username){
    throw new IllegalArgumentException("You can't create a user for a nickname you don't own.");
  }

  def this(username:String){
    this( username, None )
  }

  def this(username:String, entity_constructor:Entity){
    this( username, Option.apply(entity_constructor) )
  }
  
  def this(entity_constructor:Entity){
    this( entity_constructor.getProperty("username").asInstanceOf[String], 
          Option.apply(entity_constructor) )
  }
  
  val jsonObject:JValue = ("username" -> username )

  def getGoogleUser():GoogleUser = priv_entity.isDefined match{
    case true => { priv_entity.get.getProperty("googleUser").asInstanceOf[GoogleUser] }
    case false => {
      val userService = UserServiceFactory.getUserService();
      return userService.getCurrentUser();
    }
  }

  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => { 
      var ent = new Entity("User", username);
      ent.setUnindexedProperty("googleUser", googleUser);
      ent.setUnindexedProperty("username", username);
      priv_entity = Option.apply(ent)
      return priv_entity.get;
    }
  }
}

object User {
  def get( username:String, datastore:DatastoreService ):Option[User] = {
    return User.get(KeyFactory.createKey("User", username), datastore)
  }
  def get( key:Key, datastore:DatastoreService ):Option[User] = {
    try{
      return Option.apply(new User( datastore.get(key)));
    }
    catch{
      case e:EntityNotFoundException => {
        return None;
      }
    }
  }
  def create( username:String, datastore:DatastoreService ):User = {
    var user = new User(username)
    datastore.put(user.entity)
    return user;
  }
}
