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
		// TODO read properties
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
				System.out.println(status.getUser().getName() + " : " + status.getText());				
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
	    	   
		// TODO read properties
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    twitterStream.addListener(listener);

	    FilterQuery filter = new FilterQuery();
	    String keywords[] = {"Ayuntamiento de Madrid","Ayto Madrid", "@MADRID",
	    		"@JMD_Sanblas", "@JMDArganzuela", "@JMDBarajas", "@JMDCarabanchel",
	    		"@JMDCentro", "@JMDChamartin", "@JMDChamberi", "@JMDCiudadLineal",
	    		"@JMDFuencarral", "@jmdhortaleza", "@JMDLatina", "@MoncloaAravaca",
	    		"@JMDmoratalaz", "@JMDpvallecas", "@JMDretiro", "@JMDSalamanca",
	    		"@JMD_Sanblas", "@distritotetuan", "@JMDTetuan", "@jmd_usera",
	    		"@jmdvicalvaro", "@JMDvivallecas", "@JMD_villaverde"};
	    filter.track(keywords);
	    filter.language(new String[] { "es" });
	    
	    twitterStream.filter(filter);
	}
}
