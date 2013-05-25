package com.neodem.starman;

import static com.neodem.starman.XomHelper.addAttribute;
import static com.neodem.starman.XomHelper.addChildElement;
import static com.neodem.starman.XomHelper.addChildHtmlElement;
import static com.neodem.starman.XomHelper.makeElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public class RssWriter {
	private static final DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

	public void makeFeed(Path destFile, Collection<Article> articles, String title, String link) throws IOException {
		
		Element channel = makeElement("channel");
		addChildElement(channel, "title", title);
		addChildElement(channel, "link", link);
		addChildElement(channel, "description", "my starred items");
		addChildElement(channel, "lastBuildDate", rfc822Now());
		addChildElement(channel, "language", "en-us");

//		Iterator<Article> i = articles.iterator();
//		channel.appendChild(makeRssItem(i.next()));
//		channel.appendChild(makeRssItem(i.next()));
//		channel.appendChild(makeRssItem(i.next()));
//		channel.appendChild(makeRssItem(i.next()));
//		channel.appendChild(makeRssItem(i.next()));
//		channel.appendChild(makeRssItem(i.next()));
//		channel.appendChild(makeRssItem(i.next()));
//		channel.appendChild(makeRssItem(i.next()));

		for (Article a : articles) {
			channel.appendChild(makeRssItem(a));
		}
		
		Element root = makeElement("rss");
		addAttribute(root, "version", "2.0");
		root.appendChild(channel);
		
		Document doc = new Document(root);

		Serializer serializer = new Serializer(Files.newOutputStream(destFile), "ISO-8859-1");
		serializer.setIndent(4);
		serializer.setMaxLength(128);
		serializer.write(doc);
	}

	private Element makeRssItem(Article a) {
		Element item = makeElement("item");
		addChildElement(item, "title", a.getTitle());
		addChildElement(item, "link", a.getLink().toString());
		addChildElement(item, "guid", a.getLink().toString());
		addChildElement(item, "pubDate", rfc822(a.getPublished() * 1000));

		if (a.getContent() != null) {
			addChildHtmlElement(item, "description", a.getContent());
		} else if (a.getSummarry() != null) {
			addChildHtmlElement(item, "description", a.getSummarry());
		}

		return item;
	}
	
	private String rfc822(long time) {
		return df.format(new Date(time));
	}

	private String rfc822Now() {
		return df.format(new Date());
	}
}
