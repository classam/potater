package net.lassam

import net.liftweb.json._

trait HasJsonObject {
  def jsonObject : JValue
  def json:String = { pretty(render(jsonObject)) }
}
