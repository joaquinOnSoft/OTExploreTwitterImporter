package com.opentext.explore.importer.twitter;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.util.FileUtil;

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
	
	final static Log log  = LogFactory.getLog(TwitterImporter.class);
	
	public TwitterImporter(Properties prop) {
		this.prop = prop;
		
		String strVerbose = prop.getProperty("verbose", "true");
		verbose = Boolean.valueOf(strVerbose);
	}

	public void start() {
		// <strong>Streaming API</strong>
		//  TwitterStream class has several methods prepared for the streaming API. 
		//  All you need is to have a class implementing StatusListener. 
		//  Twitter4J will do creating a thread, consuming the stream.
		//
		//  SEE: http://twitter4j.org/en/code-examples.html#streaming
		StatusListener listener = new StatusListener(){

			@Override
			public void onException(Exception ex) {				
			}

			@Override
			public void onStatus(Status status) {
				if(verbose) {
					System.out.println(status.getUser().getName() + " : " + status.getText());					
				}
				
				String xmlFileName = Long.toString(status.getId()) + ".xml";
				try {
					TwitterTransformer.statusToXMLFile(status, xmlFileName);
					
					File xml = new File(xmlFileName); 
					
					String host = prop.getProperty("host");
					SolrAPIWrapper wrapper = null;
					if(host == null)
						wrapper = new SolrAPIWrapper();
					else {
						wrapper = new SolrAPIWrapper(host);
					}
					wrapper.otcaBatchUpdate(xml);	
				} catch (IOException e) {
					log.error(e.getMessage());
				}
				finally {
					FileUtil.deleteFile(xmlFileName);
				}
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {				
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
			}

			@Override
			public void onStallWarning(StallWarning warning) {
			}
	    };
	    	   
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    twitterStream.addListener(listener);

	    FilterQuery filter = new FilterQuery();
	    filter.track(stringToArrayString(prop.getProperty("keywords")));
	    filter.language(stringToArrayString(prop.getProperty("languages")));
	    
	    String[] followStr = stringToArrayString(prop.getProperty("follow"));
	    if(followStr != null && followStr.length > 0) {
	    	filter.follow(getUserIdByScreenName(followStr));
	    }
	    
	    twitterStream.filter(filter);
	}
	
	private String[] stringToArrayString(String value) {
		if(value != null) {
			return value.split(",");
		}
		else {
			return new String[] {};
		}
		
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
