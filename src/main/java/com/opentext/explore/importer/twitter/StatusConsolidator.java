package com.opentext.explore.importer.twitter;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.util.FileUtil;

import twitter4j.Status;

public class StatusConsolidator {
	
	protected static final Logger log = LogManager.getLogger(StatusConsolidator.class);


	public static void ingest(Status status, boolean verbose, boolean ignoreRetweet, String tag , String contentType, String host) {
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
}
