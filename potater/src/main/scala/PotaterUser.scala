package net.lassam

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import net.lassam._

trait PotaterUser {
  def subscriptions : List[Feed]
  def articles : List[ArticleStub]
  def settings : List[UserSetting]
  def articlesJson : List[JValue] = { articles.map( _.jsonObject) }
  def subscriptionsJson : List[JValue] = { subscriptions.map( _.jsonObject) }
  def settingsJson : List[JValue] = { settings.map( _.jsonObject) }
}

object PotaterUser {
  def getUser( username:String ):PotaterUser = {
    val mock = true;
    if( mock ){
      return new MockUser( username );
    }
    else{
      return new GoogleUser( username );
    }
  }
}
