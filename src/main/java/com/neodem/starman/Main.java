package com.neodem.starman;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * will read the google reader starred.json file into objects and output into a feed
 * 
 * @author vfumo
 * 
 */
public class Main {

	public Main(String[] args) throws JsonParseException, IOException {
		Path sourceFile = Paths.get(args[0]);
		Path destFile = Paths.get(args[1]);
		String title = args[2];
		String link = args[3];
		
		JsonReader r = new JsonReader();
		Collection<Article> articles = r.parseArticles(sourceFile);
		
		RssWriter a = new RssWriter();
		a.makeFeed(destFile, articles, title, link);
	}

	public static void main(String[] args) throws JsonParseException, IOException {
		new Main(args);
	}
}

