package net.lassam.test

import net.lassam._
import org.scalatest.FunSuite
import org.joda.time._

class SyndicateSuite extends FunSuite {
  test("RSS2 - Title"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).title === "RSS Title")
  }
  test("RSS2 - Link"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).link === "http://www.example.com/feed.xml")
  }
  test("RSS2 - Description"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).description === "This is an example of an RSS feed")
  }
  test("RSS2 - Copyright"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).copyright === "Copyright")
  }
  test("RSS2 - Language"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).language === "en-us")
  }
  test("RSS2 - Updated"){
    val dt = Syndicate.parse(Corpus.wikipedia_rss2).updatedDateTime;
    assert(dt.dayOfMonth().get() === 6)
    assert(dt.monthOfYear().getAsShortText() === "Sep")
    assert(dt.year().get() === 2010)
  }
  test("RSS2 - Item 1 - Title"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).items(0).title === "Example entry")
  }
  test("RSS2 - Item 1 - Description"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).items(0).content === "Here is some text containing an interesting description.")
  }
  test("RSS2 - Item 1 - Link"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).items(0).link === "http://www.example.com/")
  }
  test("RSS2 - Item 1 - Guid"){
    assert(Syndicate.parse(Corpus.wikipedia_rss2).items(0).guid === "unique string per item")
  }
  test("RSS2 - Item 1 - UpdatedDateTime"){
    val dt = Syndicate.parse(Corpus.wikipedia_rss2).items(0).updatedDateTime;
    assert(dt.dayOfMonth().get() === 7)
    assert(dt.monthOfYear().getAsShortText() === "Sep")
    assert(dt.year().get() === 2009)
    assert(dt.hourOfDay().get() === 16)
  }

  test("Atom - Title"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).title === "Example Feed")
  }
  test("Atom - Link"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).link === "http://example.org/")
  }
  test("Atom - Description"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).description === "A subtitle.")
  }
  test("Atom - Copyright"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).copyright === "")
  }
  test("Atom - Language"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).language === "")
  }
  test("Atom - Updated"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).updated === "2003-12-13T18:30:02Z") 
  }
  test("Atom - UpdatedDate"){
    val dt = Syndicate.parse(Corpus.wikipedia_atom).updatedDateTime;
    assert(dt.dayOfMonth().get() === 13)
    assert(dt.monthOfYear().getAsShortText() === "Dec")
    assert(dt.year().get() === 2003)
    assert(dt.hourOfDay().get() === 18)
  }
  test("Atom - Item 1 - Title"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).items(0).title === "Atom-Powered Robots Run Amok")
  }
  test("Atom - Item 1 - Summary"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).items(0).summary === "Some text.")
  }
  test("Atom - Item 1 - Content"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).items(0).content === "")
  }
  test("Atom - Item 1 - Link"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).items(0).link === "http://example.org/2003/12/13/atom03")
  }
  test("Atom - Item 1 - Guid"){
    assert(Syndicate.parse(Corpus.wikipedia_atom).items(0).guid === "urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a")
  }
  test("Atom - Item 1 - UpdatedDate"){
    val dt = Syndicate.parse(Corpus.wikipedia_atom).items(0).updatedDateTime;
    assert(dt.dayOfMonth().get() === 13)
    assert(dt.monthOfYear().getAsShortText() === "Dec")
    assert(dt.year().get() === 2004)
    assert(dt.hourOfDay().get() === 18)
  }
  
  test("Realityhacking - Title"){
    assert(Syndicate.parse(Corpus.realityhacking).title === "REALITYHACKING")
  }
  test("Realityhacking - Link"){
    assert(Syndicate.parse(Corpus.realityhacking).link === "http://realityhacking.net/")
  }
  test("Realityhacking - Description"){
    assert(Syndicate.parse(Corpus.realityhacking).description === "The personal blog of Angelina Fabbro. Stuff here and on my Twitter reflect my own opinions, not those of my employer Steamclock Software.");
  }
  test("Realityhacking - If there's no pubdate, use the latest item"){
    val dt = Syndicate.parse(Corpus.realityhacking).updatedDateTime;
    assert(dt.dayOfMonth().get() === 17)
    assert(dt.monthOfYear().getAsShortText() === "Aug")
    assert(dt.year().get() === 2013)
  }
  test("Realityhacking - Item 1 - Title"){
    assert(Syndicate.parse(Corpus.realityhacking).items(0).title === "An edge and wonderful re-think of Sailor Moon.I didn't...");
  }
  test("Realityhacking - Item 1 - Link"){
    assert(Syndicate.parse(Corpus.realityhacking).items(0).link === "http://realityhacking.net/post/58528332296");
  }
  test("Realityhacking - Item 1 - Content"){
    assert(Syndicate.parse(Corpus.realityhacking).items(0).content === "<img src=\"http://24.media.tumblr.com/d889592865bba5bad28ad8862786ba4d/tumblr_mq5civBy8B1qln25ao1_r3_500.jpg\"></img> <br></br> <br></br> <p>An edge and wonderful re-think of Sailor Moon.<br></br><br></br>I didn't grow up with any positive female role models. Sailor Moon and her companions were the only ones for a really long time. In retrospect I wish I'd had access to an English version of the manga; the characters in the manga are way more empowered and capable than in the anime. Still though, this was the first time I'd really found characters in a show I wanted to be like, and it was also the first time I saw a lesbian relationship depicted openly and in a positive light. This show made all the difference to me.</p>");
  }
  test("Realityhacking - Item 1 - UpdatedDate"){
    val dt = Syndicate.parse(Corpus.realityhacking).items(0).updatedDateTime;
    assert(dt.dayOfMonth().get() === 17)
    assert(dt.monthOfYear().getAsShortText() === "Aug")
    assert(dt.year().get() === 2013)
  }


}
