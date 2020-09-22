package com.opentext.explore.importer.twitter;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TwitterImporter {
	private Properties prop;

	protected static final Logger log = LogManager.getLogger(TwitterImporter.class);

	public TwitterImporter(Properties prop) {
		this.prop = prop;

	}

	public void start() {
		startStreamingAPI();
		//TODO use a thread to run the query
		//startQueryingOldTweets();
	}

	/**
	 * <strong>Search for old Tweets (from the previous week)</strong>
	 * Search for Tweets using Query class and Twitter.search(twitter4j.Query) method.
	 */
	private void startQueryingOldTweets() {
		TwitterImporterFromQueryOldTweets queryOldTweets = new TwitterImporterFromQueryOldTweets(prop);
		Thread t = new Thread(queryOldTweets, "Query old Tweets");
		t.start();
	}

	/**
	 * <strong>Streaming API</strong>
	 * TwitterStream class has several methods prepared for the streaming API.
	 * All you need is to have a class implementing StatusListener.
	 * Twitter4J will do creating a thread, consuming the stream.
	 * 
	 *  SEE: http://twitter4j.org/en/code-examples.html#streaming
	 */
	private void startStreamingAPI() {
		TwitterImporterFromStream stream = new TwitterImporterFromStream(prop);
		stream.run();
	}
	

}
