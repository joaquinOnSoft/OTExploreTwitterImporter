package com.opentext.explore.importer.twitter;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.util.StringUtil;

import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;

/**
 * <strong>Streaming API</strong>
 * TwitterStream class has several methods prepared for the streaming API.
 * All you need is to have a class implementing StatusListener.
 * Twitter4J will do creating a thread, consuming the stream.
 * 
 *  SEE: http://twitter4j.org/en/code-examples.html#streaming
 */
public class TwitterImporterFromStream extends AbstractTwitterImporter{

	protected static final Logger log = LogManager.getLogger(TwitterImporterFromStream.class);
	
	protected String[] followStr;
	
	public TwitterImporterFromStream(Properties prop) {
		super(prop);
		
		followStr = StringUtil.stringToArrayString(prop.getProperty("follow"));
	}

	public void run() {

		StatusListener listener = StatusListenerFactory.getListener(
				verbose, ignoreRetweet, tag, contentType, host);
		
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(listener);

		FilterQuery filter = new FilterQuery();
		filter.track(StringUtil.stringToArrayString(keywords));
		filter.language(languages);


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
