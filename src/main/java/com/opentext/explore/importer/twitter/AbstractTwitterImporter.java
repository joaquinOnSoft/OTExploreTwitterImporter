package com.opentext.explore.importer.twitter;

import java.util.Properties;

import com.opentext.explore.util.StringUtil;

public abstract class AbstractTwitterImporter {
	
	protected boolean verbose = false; 
	protected boolean ignoreRetweet = true;
	protected String keywords;
	protected String tag = null;
	protected String contentType = null;
	protected String host = null;
	protected String[] languages = null;
	
	public AbstractTwitterImporter(Properties prop) {
		String strVerbose = prop.getProperty("verbose", "true");
		verbose = Boolean.valueOf(strVerbose);

		String strIgnoreRetweet = prop.getProperty("ignoreretweet", "true");
		ignoreRetweet = Boolean.valueOf(strIgnoreRetweet);	
		
		keywords = prop.getProperty("keywords");
		tag = prop.getProperty("tag", "Twitter Importer");
		contentType = prop.getProperty("content_type", "Twitter"); 
		host = prop.getProperty("host");
		
		languages = StringUtil.stringToArrayString(prop.getProperty("languages"));
	}
}
