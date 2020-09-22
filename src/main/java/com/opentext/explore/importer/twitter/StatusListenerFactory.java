package com.opentext.explore.importer.twitter;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.util.FileUtil;

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
				if(verbose) {
					System.out.println(status.getUser().getName() + " : " + status.getText());					
				}

				if(status.isRetweet() && ignoreRetweet) {
					log.debug("Ignoring retweet: " + status.getId());
					return;
				}

				String xmlPath = null;
				String xmlFileName = Long.toString(status.getId()) + ".xml";
				try {
					xmlPath = TwitterTransformer.statusToXMLFile(status, xmlFileName, contentType, tag);

					SolrAPIWrapper wrapper = null;
					if(host == null)
						wrapper = new SolrAPIWrapper();
					else {
						wrapper = new SolrAPIWrapper(host);
					}
					wrapper.otcaBatchUpdate(new File(xmlPath));	
				} catch (IOException e) {
					log.error(e.getMessage());
				}
				finally {
					if(xmlPath != null) {
						FileUtil.deleteFile(xmlPath);	
					}

				}
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
