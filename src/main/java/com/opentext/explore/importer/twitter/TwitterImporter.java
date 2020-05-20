package com.opentext.explore.importer.twitter;

import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


public class TwitterImporter {
	public TwitterImporter(Properties prop) {
		
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatus(Status status) {
				System.out.println(status.getUser().getName() + " : " + status.getText());				
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				// TODO Auto-generated method stub				
			}
	    };
	    
	    
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    twitterStream.addListener(listener);

	    FilterQuery fq = new FilterQuery();
	    String keywords[] = {"Ayuntamiento de Madrid","Ayto Madrid","Madrid"};
	    fq.track(keywords);
	    fq.language(new String[] { "es" });
	    
	    twitterStream.filter(fq);
	}
}
