package net.lassam

import unfiltered.request._
import unfiltered.response._

class Feed( val title: String, val url: String, val description: String ){
}

class ArticleStub(val title: String, val sourceUrl: String, val feed: Feed, val excerpt: String) {
  
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
      val list = article :: article_two :: article_three :: Nil
      ResponseString( list.toString )
    }
    case GET(Path(Seg("users" :: username :: "subscriptions" :: Nil))) => {
      ResponseString("<h1>Hello there, "+username+". </h1>")
    }
    case GET(Path(Seg("users" :: username :: "articlestubs" :: Nil))) => {
      ResponseString("<h1>Hello there, "+username+". </h1>")
    }
  }
}
