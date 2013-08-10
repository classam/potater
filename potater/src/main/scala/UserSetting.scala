package net.lassam

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class UserSetting(val key: String, val value: String) extends HasJsonObject{
  val jsonObject:JValue = (key -> value) 
}
