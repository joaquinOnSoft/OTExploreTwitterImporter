package com.opentext.explore.importer.twitter;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.util.FileUtil;
import com.opentext.explore.util.StringUtil;

import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;


public class TwitterImporter {
	private Properties prop;
	private boolean verbose = true; 
	private boolean ignoreRetweet = true; 

	protected static final Logger log = LogManager.getLogger(TwitterImporter.class);

	public TwitterImporter(Properties prop) {
		this.prop = prop;

		String strVerbose = prop.getProperty("verbose", "true");
		verbose = Boolean.valueOf(strVerbose);

		String strIgnoreRetweet = prop.getProperty("ignoreretweet", "true");
		ignoreRetweet = Boolean.valueOf(strIgnoreRetweet);		
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
		TwitterImporterQueryOldTweets queryOldTweets = new TwitterImporterQueryOldTweets(prop);
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

		StatusListener listener = StatusListenerFactory.getListener(verbose, ignoreRetweet, 
				prop.getProperty("tag", "Twitter Importer"), 
				prop.getProperty("content_type", "Twitter"), 
				prop.getProperty("host"));
		
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(listener);

		FilterQuery filter = new FilterQuery();
		filter.track(StringUtil.stringToArrayString(prop.getProperty("keywords")));
		filter.language(StringUtil.stringToArrayString(prop.getProperty("languages")));

		String[] followStr = StringUtil.stringToArrayString(prop.getProperty("follow"));
		if(followStr != null && followStr.length > 0) {
			filter.follow(getUserIdByScreenName(followStr));
		}

		twitterStream.filter(filter);
	}
	
	protected long[] getUserIdByScreenName(String[] screenName) {
		long[] ids = {};

		Twitter t = new TwitterFactory().getInstance();
		ResponseList<User> users = null;
		try {
			users = t.lookupUsers(screenName);
		} catch (TwitterException e) {
			log.error(e);
		}
		if(users != null) {
			ids = new long[users.size()];
			int i= 0;
			for (User usr: users) {
				ids[i] =usr.getId();
				i++;
			}

		}

		return ids;
	}
}
