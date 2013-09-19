package net.lassam

import net.lassam._

import scala.collection.JavaConverters._
import java.util.{Date => OldDate}

import com.google.appengine.api.datastore._
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class QQ{

}

class Databarge[T <: EntityWrapper]( datastore:Datastore ){

  def create(key:Key, obj:T){

  }
  def get(key:Key):Option[T] = {

  }
  def query(query:QQ):Iterable[T] = {

  }


}

sealed abstract class Property (val name:String, val isIndexed:Boolean)
  case class StringProperty(name:String, isIndexed:Boolean, val value:Option[String]) extends Property(name, isIndexed)
  case class DateTimeProperty(name:String, isIndexed:Boolean, val value:Option[DateTime]) extends Property(name, isIndexed) 
  case class KeyProperty(name:String, isIndexed:Boolean, val value:Option[Key]) extends Property(name, isIndexed)
  case class TextProperty(name:String, isIndexed:Boolean, val value:Option[String]) extends Property(name, isIndexed)

abstract class EntityWrapper(entity_constructor:Option[Entity]){
  var priv_entity:Option[Entity] = entity_constructor;
  if( priv_entity.isDefined ){
    deserialize( priv_entity.get )
  }

  def properties:List[Property]

  def deserialize(e:Entity)= {

  }
  def serialize:Entity = {

  }
  def get(propertyName:String):Property = {

  }
  def set(property:Property) = match {
    case StringProperty = {
    }

  }

  def entity:Entity = priv_entity.isDefined match {
    case true => { priv_entity.get }
    case false => {
      priv_entity = Option.apply( serialize ) 
      return priv_entity.get
    }
  }
  def key:Key = { entity.getKey }
}

