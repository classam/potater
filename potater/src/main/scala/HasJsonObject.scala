package net.lassam

import net.liftweb.json._

trait HasJsonObject {
  def jsonObject : JValue
  def json:String = { pretty(render(jsonObject)) }
}

object HasJsonObject {
  def listJson(iterable:Iterable[HasJsonObject]):String = {
    pretty(render(new JArray(iterable.map(_.jsonObject).toList)))
  }
  def listJson(list:List[HasJsonObject]):String = {
    pretty(render(new JArray(list.map(_.jsonObject))))
  }
}
