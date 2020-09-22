package com.opentext.explore.importer.twitter;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class StatusListenerFactory {
	protected static final Logger log = LogManager.getLogger(StatusListenerFactory.class);
	
	
	public static StatusListener getListener(boolean verbose, boolean ignoreRetweet, String tag , String contentType, String host) {

		StatusListener listener = new StatusListener(){

			@Override
			public void onException(Exception ex) {	
				log.error("Exception on status listener: " + ex.getLocalizedMessage());
			}

			@Override
			public void onStatus(Status status) {
				StatusConsolidator.ingest(status, verbose, ignoreRetweet, tag, contentType, host);
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				log.info("onDeletionNotice: " + statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				log.info("onTrackLimitationNotice: " + numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				log.info("onScrubGeo: user id: " + userId + " up to Status id: " + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				log.info("onStallWarning: " + warning.getMessage());				
			}
		};

		return listener;	
	}
}
