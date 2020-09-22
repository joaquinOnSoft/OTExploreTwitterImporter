package com.opentext.explore.importer.twitter;

import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.util.DateUtil;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * <strong>Search for old Tweets (from the previous week)</strong>
 * Search for Tweets using Query class and Twitter.search(twitter4j.Query) method.
 */
public class TwitterImporterFromQueryOldTweets extends AbstractTwitterImporter implements Runnable {
	private static final int MILISECONDS_IN_SECOND = 1000;
	private static final int SECONDS_IN_15_MIN = 900;

	/**
	 * 429:Returned in API v1.1 when a request cannot be served due to the application's 
	 * rate limit having been exhausted for the resource. See Rate Limiting in 
	 * API v1.1.(https://dev.twitter.com/docs/rate-limiting/1.1) message - Rate limit exceeded
	 * 
	 * code - 88
	 */
	private static final int STATUS_CODE_RATE_LIMIT = 429;
	
	protected static final Logger log = LogManager.getLogger(TwitterImporter.class);

	public TwitterImporterFromQueryOldTweets(Properties prop) {
		super(prop);		
	}
	
	@Override
	public void run() {
		// The factory instance is re-useable and thread safe.
		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query();

		String queryString= "";

		if(keywords != null) {
			queryString +=  keywords.replace(",", " OR ");
		}

		/*
		 * String follow = prop.getProperty("follow"); if(follow != null) { queryString
		 * += " from: " + follow.replace(",", " "); }
		 */

		log.debug("QUERY STRING: " + queryString);

		query.setQuery(queryString);
		query.setSince(DateUtil.getDateOneWeekAgo());

		if(languages != null && languages.length > 0) {
			query.setLang(languages[0]);	
		}	    

		QueryResult result = null;

		do {
			try {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					log.debug("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
					
					StatusConsolidator.ingest(tweet, verbose, ignoreRetweet, tag, contentType, host);					
				}
			} catch (TwitterException te) {
				log.error(te);

				if(te.getStatusCode() == STATUS_CODE_RATE_LIMIT) {
					log.error("RATE LIMIT REACHED >>>>>>>>>>>>>>>>>>>>> ");
					int seconds = te.getRetryAfter();
					log.debug("Retry after " + seconds + " seconds");

					if(seconds < 0) {
						seconds = SECONDS_IN_15_MIN;
					}
					log.info("Sleeping " + seconds + " seconds");

					try {
						Thread.sleep(seconds * MILISECONDS_IN_SECOND);
					} catch (InterruptedException e) {
						log.error("Fail on sleep: " + e.getMessage());
						System.exit(-1);
					}
				}
				else {
					log.error("Failed to search tweets: " + te.getMessage());
					System.exit(-1);	    		
				}

			}
		} while ((query = result.nextQuery()) != null);
	}
}
