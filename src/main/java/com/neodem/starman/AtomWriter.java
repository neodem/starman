package com.neodem.starman;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import nu.xom.Text;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class AtomWriter {
	private static final String namespace = "http://www.w3.org/2005/Atom";

	public void makeFeed(Path destFile, Collection<Article> articles) throws IOException {
		Element root = new Element("feed", namespace);
		root.appendChild(makeNode("title", "myfeed"));

		root.appendChild(makeNode("updated", rfc3339Now()));

		root.appendChild(makeId());

		Element link = new Element("link", namespace);
		link.addAttribute(new Attribute("href", "http://www.neodem.com/out.xml"));
		link.addAttribute(new Attribute("rel", "self"));
		root.appendChild(link);

		Iterator<Article> i = articles.iterator();
		root.appendChild(makeEntry(i.next()));
		root.appendChild(makeEntry(i.next()));
		root.appendChild(makeEntry(i.next()));
		root.appendChild(makeEntry(i.next()));
		root.appendChild(makeEntry(i.next()));
		root.appendChild(makeEntry(i.next()));
		root.appendChild(makeEntry(i.next()));
		root.appendChild(makeEntry(i.next()));

		// for (Article a : articles) {
		// root.appendChild(makeEntry(a));
		// }

		Document doc = new Document(root);

		Serializer serializer = new Serializer(Files.newOutputStream(destFile), "ISO-8859-1");
		serializer.setIndent(4);
		serializer.setMaxLength(128);
		serializer.write(doc);
	}

	private String rfc3339Now() {
		DateTime dt = new DateTime();
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		return fmt.print(dt);
	}

	private String rfc3339(long time) {
		DateTime dt = new DateTime(time);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		return fmt.print(dt);
	}

	private static final Random r = new Random(System.currentTimeMillis());

	private Element makeId() {
		Element id = new Element("id", namespace);

		UUID uuid = new UUID(r.nextLong(), r.nextLong());

		id.appendChild("urn:uuid:" + uuid.toString());
		return id;
	}

	private Element makeEntry(Article a) {
		Element entry = new Element("entry", namespace);

		entry.appendChild(makeNode("title", a.getTitle()));
		entry.appendChild(makeLink(a.getLink()));

		entry.appendChild(makeNode("updated", rfc3339(a.getPublished() * 1000)));

		Element author = new Element("author", namespace);
		author.appendChild(makeNode("name", "author"));
		entry.appendChild(author);

		entry.appendChild(makeId());

		if (a.getSummarry() != null) {
			addHtml(a.getSummarry(), "summary", entry);
		}

		if (a.getContent() != null) {
			addHtml(a.getContent(), "content", entry);
		}

		return entry;
	}

	/**
	 * @param a
	 * @param entry
	 */
	private void addHtml(String html, String name, Element entry) {
		Element content = new Element(name, namespace);
		content.addAttribute(new Attribute("type", "html"));
		Text cdata = new Text(html);
		content.appendChild(cdata);
		entry.appendChild(content);
	}

	private Element makeLink(URL url) {
		Element link = new Element("link", namespace);
		link.addAttribute(new Attribute("href", url.toString()));
		return link;
	}

	private Element makeNode(String name, String value) {
		Element node = new Element(name, namespace);
		node.appendChild(value);
		return node;
	}
}
