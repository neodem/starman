package com.neodem.starman;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonReader {
	public Collection<Article> parseArticles(Path sourceFile) throws JsonParseException, IOException {

		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jp = jsonFactory.createParser(sourceFile.toFile());
		Collection<Article> items = null;
		if (jp.nextToken() != JsonToken.START_OBJECT) {
			throw new IOException("Expected data to start with an Object");
		}
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			if (jp.getCurrentName().equals("items")) {
				jp.nextToken();
				items = getArticlesFromItemsArray(jp);
			}
		}
		jp.close();
		return items;
	}

	private Collection<Article> getArticlesFromItemsArray(JsonParser jp) throws JsonParseException, IOException {
		JsonToken token = jp.getCurrentToken();
		if (token != JsonToken.START_ARRAY) {
			throw new IOException("Expected to find an array here");
		}
		token = jp.nextToken();

		Collection<Article> articles = new ArrayList<>();
		while (token != JsonToken.END_ARRAY) {
			articles.add(readArticle(jp));
			token = jp.nextToken();
		}
		return articles;
	}

	private Article readArticle(JsonParser jp) throws IOException {
		JsonToken token = jp.getCurrentToken();
		if (token != JsonToken.START_OBJECT) {
			throw new IOException("Expected to find an object here");
		}

		Article result = new Article();

		while (token != JsonToken.END_OBJECT) {

			if (token == JsonToken.FIELD_NAME) {
				handleField(jp, result);
			}
			token = jp.nextToken();
		}
		return result;
	}

	/**
	 * @param jp
	 * @param result
	 * @throws IOException
	 * @throws JsonParseException
	 */
	private void handleField(JsonParser jp, Article result) throws IOException, JsonParseException {
		String fieldName = jp.getCurrentName();
		jp.nextToken();
		if (fieldName.equals("timestampUsec")) {
			result.setTimestamp(jp.getValueAsLong());
		} else if (fieldName.equals("title")) {
			String title = jp.getValueAsString();
			result.setTitle(title);
			System.out.println("->" + title);
		} else if (fieldName.equals("published")) {
			result.setPublished(jp.getValueAsLong());
		} else if (fieldName.equals("updated")) {
			result.setUpdated(jp.getValueAsLong());
		} else if (fieldName.equals("alternate")) {
			result.setLink(getLink(jp));
		} else if (fieldName.equals("summary")) {
			result.setSummarry(getContent(jp));
		} else if (fieldName.equals("comments")) {
			eatTillEndOfArray(jp);
		} else if (fieldName.equals("annotations")) {
			eatTillEndOfArray(jp);
		} else if (fieldName.equals("annotations")) {
			eatTillEndOfArray(jp);
		} else if (fieldName.equals("categories")) {
			eatTillEndOfArray(jp);
		} else if (fieldName.equals("origin")) {
			eatTillEndObjext(jp);
		} else if (fieldName.equals("content")) {
			result.setContent(getContent(jp));
		} else if (fieldName.equals("canonical")) {
			eatTillEndOfArray(jp);
		} else if (fieldName.equals("replies")) {
			eatTillEndOfArray(jp);
		} else if (fieldName.equals("enclosure")) {
			eatTillEndOfArray(jp);
		} else if (fieldName.equals("related")) {
			eatTillEndOfArray(jp);
		}
	}

	private void eatTillEndOfArray(JsonParser jp) throws JsonParseException, IOException {
		while (jp.nextToken() != JsonToken.END_ARRAY)
			;
	}

	private void eatTillEndObjext(JsonParser jp) throws JsonParseException, IOException {
		while (jp.nextToken() != JsonToken.END_OBJECT)
			;
	}

	private String getContent(JsonParser jp) throws IOException {
		if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
			throw new IOException("Expected to find an object here");
		}

		String content = null;

		while (jp.nextToken() != JsonToken.END_OBJECT) {
			if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
				if (jp.getCurrentName().equals("content")) {
					jp.nextToken();
					content = jp.getValueAsString();
				}
			}
		}

		return content;
	}

	private URL getLink(JsonParser jp) throws JsonParseException, IOException {
		if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
			throw new IOException("Expected to find an array here");
		}

		URL ref = null;

		while (jp.nextToken() != JsonToken.END_ARRAY) {
			if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
				if (jp.getCurrentName().equals("href")) {
					jp.nextToken();
					ref = new URL(jp.getValueAsString());
				}
			}
		}
		return ref;
	}
}
