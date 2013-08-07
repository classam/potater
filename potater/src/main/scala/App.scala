package net.lassam

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

class Feed( val title: String, val url: String, val description: String ){
  val json_object = ("title"-> title) ~ ("url"-> url) ~ ("description"-> description)
}

class ArticleStub(val title: String, val sourceUrl: String, val feed: Feed, val excerpt: String) {
  val json_object = ("title" -> title ) ~ ("sourceUrl" -> sourceUrl) ~ ("feed" -> feed.json_object) ~ ("excerpt" -> excerpt )
}

/** unfiltered plan */
class App extends unfiltered.filter.Plan {
  import QParams._

  def intent = {
    case req @ Path("/") => req match {
      case GET(_) => {
        Html(<h1>Hello World</h1>)
      }
      case _ => {
        Html(<h1>Oh noooooo!</h1>)
      }
    }
    case GET(Path("/users")) => {
      Html(<h1>Oh YEAH</h1>)
    }
    case GET(Path(Seg("users" :: username :: Nil) ) ) => {
      val lassamFeed = new Feed("Curtis Lassam's Website", "http://curtis.lassam.net/rss.xml", "Cube Dronery")
      val article = new ArticleStub("Vim Learnings", "http://curtis.lassam.net/post/2013_07_29-Vim_Learnings.html", lassamFeed, "Excerpt")
      val article_two = new ArticleStub("More Vim Learnings", "http://curtis.lassam.net/post/2013_07_29-Vim_Learnings.html", lassamFeed, "Excerpt")
      val article_three = new ArticleStub("Even More Vim Learnings", "http://curtis.lassam.net/post/2013_07_29-Vim_Learnings.html", lassamFeed, "Excerpt")
      val list = article.json_object :: article_two.json_object :: article_three.json_object :: Nil
      ResponseString( pretty(render(list)) )
    }
    case GET(Path(Seg("users" :: username :: "subscriptions" :: Nil))) => {
      ResponseString("<h1>Hello there, "+username+". </h1>")
    }
    case GET(Path(Seg("users" :: username :: "articlestubs" :: Nil))) => {
      ResponseString("<h1>Hello there, "+username+". </h1>")
    }
  }
}
