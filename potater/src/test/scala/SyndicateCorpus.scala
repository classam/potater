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

val garbage = <rss version="2.0">
  <channel>
    <lastBuildDate>FRAPP BLARP BLOOP</lastBuildDate>
    <pubDate>GURGLE SPUDGE</pubDate>
    <item>
      <link>http://www.example.com/</link>
    </item>
  </channel>
</rss>

val machismo_rss1 = <rss xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:wfw="http://wellformedweb.org/CommentAPI/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:sy="http://purl.org/rss/1.0/modules/syndication/" xmlns:slash="http://purl.org/rss/1.0/modules/slash/" version="2.0">
<channel>
<title>Manly Guys Doing Manly Things</title>
<atom:link href="http://thepunchlineismachismo.com/feed" rel="self" type="application/rss+xml"/>
<link>http://thepunchlineismachismo.com</link>
<description>Updated Mondays or whenever I feel like it</description>
<lastBuildDate>Fri, 16 Aug 2013 21:20:05 +0000</lastBuildDate>
<language>en-US</language>
<sy:updatePeriod>hourly</sy:updatePeriod>
<sy:updateFrequency>1</sy:updateFrequency>
<generator>http://wordpress.org/?v=3.5.1</generator>
<item>
<title>
I had a really hard time coming up with something even moderately funny this week
</title>
<link>
http://thepunchlineismachismo.com/archives/comic/i-had-a-really-hard-time-coming-up-with-something-even-moderately-funny-this-week
</link>
<comments>
http://thepunchlineismachismo.com/archives/comic/i-had-a-really-hard-time-coming-up-with-something-even-moderately-funny-this-week#comments
</comments>
<pubDate>Fri, 16 Aug 2013 21:20:05 +0000</pubDate>
<dc:creator>Coelasquid</dc:creator>
<guid isPermaLink="false">http://thepunchlineismachismo.com/?post_type=comic</guid>
<description>
<![CDATA[
<p><a href="http://thepunchlineismachismo.com/archives/comic/i-had-a-really-hard-time-coming-up-with-something-even-moderately-funny-this-week" rel="bookmark" title="I had a really hard time coming up with something even moderately funny this week"><img width="150" height="150" src="http://thepunchlineismachismo.com/wp-content/uploads/2013/08/2013-08-12-150x150.jpg" class="attachment-thumbnail wp-post-image" alt="2013-08-12" /></a> </p>I used all my comedy energy up drawing pigeons on tumblr. I feel like the better computers get the less people who use computers necessarily need to know about the workings of computers. Case in point, I am using a <a class="more-link" href="http://thepunchlineismachismo.com/archives/comic/i-had-a-really-hard-time-coming-up-with-something-even-moderately-funny-this-week"> Read the rest of this entry...</a>
]]>
</description>
<content:encoded>
<![CDATA[
<p><a href="http://thepunchlineismachismo.com/archives/comic/i-had-a-really-hard-time-coming-up-with-something-even-moderately-funny-this-week" rel="bookmark" title="I had a really hard time coming up with something even moderately funny this week"><img width="150" height="150" src="http://thepunchlineismachismo.com/wp-content/uploads/2013/08/2013-08-12-150x150.jpg" class="attachment-thumbnail wp-post-image" alt="2013-08-12" /></a> </p><p>I used all my comedy energy up <a href="http://coelasquid.tumblr.com/post/57956386511/i-have-never-seen-a-lady-pigeon-who-looked">drawing pigeons on tumblr</a>.</p> <p>I feel like the better computers get the less people who use computers necessarily need to know about the workings of computers. Case in point, I am using a computer right now.</p>
]]>
</content:encoded>
<wfw:commentRss>
http://thepunchlineismachismo.com/archives/comic/i-had-a-really-hard-time-coming-up-with-something-even-moderately-funny-this-week/feed
</wfw:commentRss>
<slash:comments>72</slash:comments>
</item>
<item>
<title>You win: Another Pacific Rim Comic</title>
<link>
http://thepunchlineismachismo.com/archives/comic/you-win-another-pacific-rim-comic
</link>
<comments>
http://thepunchlineismachismo.com/archives/comic/you-win-another-pacific-rim-comic#comments
</comments>
<pubDate>Fri, 09 Aug 2013 07:54:03 +0000</pubDate>
<dc:creator>Coelasquid</dc:creator>
<category>
<![CDATA[ atlas ]]>
</category>
<category>
<![CDATA[ mako mori ]]>
</category>
<category>
<![CDATA[ p-body ]]>
</category>
<category>
<![CDATA[ pacific rim ]]>
</category>
<category>
<![CDATA[ Portal ]]>
</category>
<category>
<![CDATA[ raleigh becket ]]>
</category>
<category>
<![CDATA[ stacker pentecost ]]>
</category>
<guid isPermaLink="false">http://thepunchlineismachismo.com/?post_type=comic</guid>
<description>
<![CDATA[
<p><a href="http://thepunchlineismachismo.com/archives/comic/you-win-another-pacific-rim-comic" rel="bookmark" title="You win: Another Pacific Rim Comic"><img width="150" height="150" src="http://thepunchlineismachismo.com/wp-content/uploads/2013/08/2013-08-05-150x150.jpg" class="attachment-thumbnail wp-post-image" alt="2013-08-05" /></a> </p>I've never tried to beat the portal multiplayer because I can't even fathom how frustrating it would be to try to get your reasoning in line with someone else's to root out the solutions. I mean, it's probably not that <a class="more-link" href="http://thepunchlineismachismo.com/archives/comic/you-win-another-pacific-rim-comic"> Read the rest of this entry...</a>
]]>
</description>
<content:encoded>
<![CDATA[
<p><a href="http://thepunchlineismachismo.com/archives/comic/you-win-another-pacific-rim-comic" rel="bookmark" title="You win: Another Pacific Rim Comic"><img width="150" height="150" src="http://thepunchlineismachismo.com/wp-content/uploads/2013/08/2013-08-05-150x150.jpg" class="attachment-thumbnail wp-post-image" alt="2013-08-05" /></a> </p><p>I've never tried to beat the portal multiplayer because I can't even fathom how frustrating it would be to try to get your reasoning in line with someone else's to root out the solutions. I mean, it's probably not that bad, but I've seen friendships nearly end over trying to steer a canoe together, compared to that team portal sounds like something multigenerational feuds would start over.</p>
]]>
</content:encoded>
<wfw:commentRss>
http://thepunchlineismachismo.com/archives/comic/you-win-another-pacific-rim-comic/feed
</wfw:commentRss>
<slash:comments>93</slash:comments>
</item>
<item>
<title>Maybe he just wasn't paying attention</title>
<link>
http://thepunchlineismachismo.com/archives/comic/maybe-he-just-wasnt-paying-attention
</link>
<comments>
http://thepunchlineismachismo.com/archives/comic/maybe-he-just-wasnt-paying-attention#comments
</comments>
<pubDate>Mon, 29 Jul 2013 10:53:45 +0000</pubDate>
<dc:creator>Coelasquid</dc:creator>
<category>
<![CDATA[ mako mori ]]>
</category>
<category>
<![CDATA[ pacific rim ]]>
</category>
<category>
<![CDATA[ raleigh becket ]]>
</category>
<guid isPermaLink="false">http://thepunchlineismachismo.com/?post_type=comic</guid>
<description>
<![CDATA[
<p><a href="http://thepunchlineismachismo.com/archives/comic/maybe-he-just-wasnt-paying-attention" rel="bookmark" title="Maybe he just wasn't paying attention"><img width="150" height="150" src="http://thepunchlineismachismo.com/wp-content/uploads/2013/07/2013-07-29-150x150.jpg" class="attachment-thumbnail wp-post-image" alt="2013-07-29" /></a> </p>I'm sure this is going to bring out the WELL ACTUALLY~ peanut gallery but I've gone to the theatre to see this movie twice and I still didn't catch any explanation for why Raleigh wouldn't know they had a sword.
]]>
</description>
<content:encoded>
<![CDATA[
<p><a href="http://thepunchlineismachismo.com/archives/comic/maybe-he-just-wasnt-paying-attention" rel="bookmark" title="Maybe he just wasn't paying attention"><img width="150" height="150" src="http://thepunchlineismachismo.com/wp-content/uploads/2013/07/2013-07-29-150x150.jpg" class="attachment-thumbnail wp-post-image" alt="2013-07-29" /></a> </p><p>I'm sure this is going to bring out the WELL <em>ACTUALLY</em>~ peanut gallery but I've gone to the theatre to see this movie twice and I still didn't catch any explanation for why Raleigh wouldn't know they had a sword.</p>
]]>
</content:encoded>
<wfw:commentRss>
http://thepunchlineismachismo.com/archives/comic/maybe-he-just-wasnt-paying-attention/feed
</wfw:commentRss>
<slash:comments>250</slash:comments>
</item>
</channel>
</rss>

val penny_arcade = <rss version="2.0">
<channel>
<title>Penny Arcade</title>
<link>http://www.penny-arcade.com</link>
<description>News Fucker 6000</description>
<language>en-us</language>
<copyright>Copyright (c) 1998-2013 Penny Arcade, Inc.</copyright>
<lastBuildDate>2013-08-16T07:01:56+00:00</lastBuildDate>
<item>
<title>
<![CDATA[ News Post: Innovationes ]]>
</title>
<link>http://penny-arcade.com/2013/08/16/innovationes</link>
<description>
<![CDATA[
Tycho: We&#8217;ve been playing Splinter Cell: Blacklist, mostly independently but on occasion together, learning just how spectacularly it is possible to fail when attempting to sneak anywhere in tandem. Splinter Cell has always been more or less my jam, and whenever someone asks me what my favorite game is I am typically forced to choose between Chaos Theory and whichever other one comes to mind at that particular time.&nbsp; It was such a cross section of gameplay - it&#8217;s more or less three excellent games - that Double Agent was kind of doomed.&nbsp; Sometimes you can&#8217;t do the same&hellip;
]]>
</description>
<author>tycho@penny-arcade.com (Tycho)</author>
<pubDate>2013-08-16T07:01:56+00:00</pubDate>
<guid>/2013/08/16/innovationes</guid>
</item>
</channel>
</rss>

}
