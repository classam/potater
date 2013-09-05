package net.lassam

import net.lassam._

import com.google.appengine.api.datastore.{DatastoreService, DatastoreServiceFactory, Entity, Key, KeyFactory, EntityNotFoundException}

class FeedService(){
  private var datastore:DatastoreService = DatastoreServiceFactory.getDatastoreService();

  def addFeed( feed:Feed ) = {

  }
  def getUpdateQueue(n:Integer):List[Feed] = {
    // Get n feeds that have not been processed recently.
    Nil
  }

}
