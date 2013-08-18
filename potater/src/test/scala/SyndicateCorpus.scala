package net.lassam.test

object Corpus {
val wikipedia_rss2 = <rss version="2.0">
  <channel>
    <title>RSS Title</title>
    <description>This is an example of an RSS feed</description>
    <link>http://www.example.com/feed.xml</link>
    <copyright>Copyright</copyright>
    <language>en-us</language>
    <lastBuildDate>Mon, 06 Sep 2010 00:01:00 +0000</lastBuildDate>
    <pubDate>Mon, 06 Sep 2009 16:20:00 +0000</pubDate>
    <ttl>1800</ttl>
    
    <item>
      <title>Example entry</title>
      <description>Here is some text containing an interesting description.</description>
      <link>http://www.example.com/</link>
      <guid>unique string per item</guid>
      <pubDate>Mon, 07 Sep 2009 16:20:00 +0000</pubDate>
    </item>
  </channel>
</rss>

val wikipedia_atom = <feed xmlns="http://www.w3.org/2005/Atom">
        <title>Example Feed</title>
        <subtitle>A subtitle.</subtitle>
        <link href="http://example.org/feed/" rel="self" />
        <link href="http://example.org/" />
        <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>
        <updated>2003-12-13T18:30:02Z</updated>
 
        <entry>
                <title>Atom-Powered Robots Run Amok</title>
                <link href="http://example.org/2003/12/13/atom03" />
                <link rel="alternate" type="text/html" href="http://example.org/2003/12/13/atom03.html"/>
                <link rel="edit" href="http://example.org/2003/12/13/atom03/edit"/>
                <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>
                <updated>2004-12-13T18:30:02Z</updated>
                <summary>Some text.</summary>
                <author>
                      <name>John Doe</name>
                      <email>johndoe@example.com</email>
                </author>
        </entry>
</feed>

val realityhacking = <rss xmlns:dc="http://purl.org/dc/elements/1.1/" version="2.0">
<channel>
<description>The personal blog of Angelina Fabbro. Stuff here and on my Twitter reflect my own opinions, not those of my employer Steamclock Software.</description>
<title>REALITYHACKING</title>
<generator>Tumblr (3.0; @angelinafabbro)</generator>
<link>http://realityhacking.net/</link>
<item>
<title>An edge and wonderful re-think of Sailor Moon.I didn't...</title>
<description><img src="http://24.media.tumblr.com/d889592865bba5bad28ad8862786ba4d/tumblr_mq5civBy8B1qln25ao1_r3_500.jpg"/><br/><br/><p>An edge and wonderful re-think of Sailor Moon.<br/><br/>I didn't grow up with any positive female role models. Sailor Moon and her companions were the only ones for a really long time. In retrospect I wish I'd had access to an English version of the manga; the characters in the manga are way more empowered and capable than in the anime. Still though, this was the first time I'd really found characters in a show I wanted to be like, and it was also the first time I saw a lesbian relationship depicted openly and in a positive light. This show made all the difference to me.</p></description>
<link>http://realityhacking.net/post/58528332296</link>
<guid>http://realityhacking.net/post/58528332296</guid>
<pubDate>Sat, 17 Aug 2013 12:08:43 -0700</pubDate>
<category>sailor moon</category>
</item>
<item>
<title>An edge and wonderful re-think of Sailor Moon.I didn't...</title>
<description><img src="http://24.media.tumblr.com/d889592865bba5bad28ad8862786ba4d/tumblr_mq5civBy8B1qln25ao1_r3_500.jpg"/><br/><br/><p>An edge and wonderful re-think of Sailor Moon.<br/><br/>I didn't grow up with any positive female role models. Sailor Moon and her companions were the only ones for a really long time. In retrospect I wish I'd had access to an English version of the manga; the characters in the manga are way more empowered and capable than in the anime. Still though, this was the first time I'd really found characters in a show I wanted to be like, and it was also the first time I saw a lesbian relationship depicted openly and in a positive light. This show made all the difference to me.</p></description>
<link>http://realityhacking.net/post/58528332296</link>
<guid>http://realityhacking.net/post/58528332296</guid>
<pubDate>Sat, 16 Aug 2013 12:08:43 -0700</pubDate>
<category>sailor moon</category>
</item>
</channel>
</rss>

}
