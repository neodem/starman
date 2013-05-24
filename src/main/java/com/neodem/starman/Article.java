package com.neodem.starman;

import java.net.URL;

public class Article {
	private long timestamp;
	private String title;
	private long published;
	private long updated;
	private URL link;
	private String content;
	private String summarry;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPublished() {
		return published;
	}

	public void setPublished(long published) {
		this.published = published;
	}

	public long getUpdated() {
		return updated;
	}

	public void setUpdated(long updated) {
		this.updated = updated;
	}

	public URL getLink() {
		return link;
	}

	public void setLink(URL link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSummarry() {
		return summarry;
	}

	public void setSummarry(String summarry) {
		this.summarry = summarry;
	}

}
