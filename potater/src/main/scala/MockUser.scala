package net.lassam

import net.lassam._

class MockUser ( val username: String ) extends PotaterUser{
  private val curtisFeed = new Feed("Curtis Lassam's Website", "http://curtis.lassam.net/rss.xml", "Cube Dronery")
  private val pvpFeed = new Feed("PVP Online", "pvponline.com/feed", "PvP News and Comics Feed")
  private val vimArticle = new ArticleStub("Hello World", "curtis.lassam.net/post/2013_07_29-Vim_Learnings.html", curtisFeed, "I'm in vim and...") 
  private val pvpArticle = new ArticleStub("Comic: Family Ties", "http://pvponline.com/comic/2013/08/06/family-ties2", pvpFeed, "Scott R. Kurtz: Last week I won Diablo III...") 
  private val setting = new UserSetting("awesome", "yes")

  def subscriptions : List[Feed] = { curtisFeed :: pvpFeed :: Nil }
  def articles : List[ArticleStub] = { vimArticle :: pvpArticle :: Nil}
  def settings: List[UserSetting] = { setting :: Nil }
}
