package net.lassam

import net.liftweb.json._

trait HasJsonObject {
  def jsonObject : JValue
}
