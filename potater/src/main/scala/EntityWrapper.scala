package net.lassam

import net.lassam._

import scala.collection.JavaConverters._
import java.util.{Date => OldDate}

import scala.language.dynamics

import com.google.appengine.api.datastore._
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Query._
import org.joda.time._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

/*
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
*/

sealed abstract class Property (val name:String)
  case class IndexedStringProperty(c_name:String) extends Property(c_name)
  case class IndexedDateTimeProperty(c_name:String) extends Property(c_name) 
  case class IndexedKeyProperty(c_name:String) extends Property(c_name)
  case class StringProperty(c_name:String) extends Property(c_name)
  case class DateTimeProperty(c_name:String) extends Property(c_name) 
  case class KeyProperty(c_name:String) extends Property(c_name)
  case class TextProperty(c_name:String) extends Property(c_name)

class PropertyNotFoundException(msg:String) extends RuntimeException(msg)
class PropertyBadlyTypedException(msg:String) extends RuntimeException(msg)

abstract class EntityWrapper(entity_constructor:Option[Entity]) extends Dynamic with HasJsonObject{
  private var privEntity:Option[Entity] = entity_constructor;

  def properties:List[Property]
  def entityName:String
  def keyString:String
  def parentKey:Option[Key]

  def key:Key = { KeyFactory.createKey(entityName, keyString) }

  private def getProperty(fieldName:String):Property = {
    for( property <- properties ){
      if( property.name == fieldName ){
        return property
      }
    }
    throw new PropertyNotFoundException( fieldName ) 
  }
  def selectDynamic(fieldName:String) = {
    selectWithProperty( getProperty(fieldName) )
  }
  private def selectWithProperty(property:Property) = property match {
    case IndexedStringProperty(_) => {
      entity.getProperty(property.name).asInstanceOf[String]
    } 
    case IndexedDateTimeProperty(_) => {
      new DateTime(entity.getProperty(property.name).asInstanceOf[OldDate]) 
    }
    case IndexedKeyProperty(_) => {
      entity.getProperty(property.name).asInstanceOf[Key]
    }
    case StringProperty(_) => { 
      entity.getProperty(property.name).asInstanceOf[String]
    }
    case DateTimeProperty(_) => {
      new DateTime(entity.getProperty(property.name).asInstanceOf[OldDate]) 
    }
    case KeyProperty(_) => {
      entity.getProperty(property.name).asInstanceOf[Key]
    }
    case TextProperty(_) => {
      entity.getProperty(property.name).asInstanceOf[Text].getValue
    }
    case _ => {
      throw new PropertyBadlyTypedException( property.name ) 
    }
  }
  
  def updateDynamic(fieldName:String, value: Any) = {
    updateWithProperty( getProperty(fieldName), value )
  } 
  private def updateWithProperty(property:Property, value:Any) = (property, value) match {
    case (IndexedStringProperty(_), value:String) => { 
      entity.setProperty(property.name, value)
    }
    case (IndexedDateTimeProperty(_), value:DateTime) => {
      entity.setProperty(property.name, value.toDate)
    }
    case (IndexedKeyProperty(_), value:Key) => {
      entity.setProperty(property.name, value)
    }
    case (StringProperty(_), value:String) => { 
      entity.setUnindexedProperty(property.name, value)
    }
    case (DateTimeProperty(_), value:DateTime) => {
      entity.setUnindexedProperty(property.name, value.toDate)
    }
    case (KeyProperty(_), value:Key) => {
      entity.setUnindexedProperty(property.name, value)
    }
    case (TextProperty(_), value:String) => {
      entity.setUnindexedProperty(property.name, new Text(value))
    }
    case (_, _) => {
      throw new PropertyBadlyTypedException( property.name ) 
    }
  }

  def entity:Entity = privEntity.isDefined match {
    case true => { privEntity.get }
    case false => {
      if( parentKey.isDefined ){
        privEntity = Option.apply(new Entity(entityName, keyString, parentKey.get )) 
      }
      else{
        privEntity = Option.apply(new Entity(entityName, keyString ))
      }
      return privEntity.get
    }
  }

  private def selectJson( property:Property ):JObject = {
    (property.name -> selectWithProperty(property).toString() )
  }

  def jsonObject = {
    properties.map(selectJson).reduce( (left:JObject, right:JObject) => {left ~ right} ) 
  }
}

