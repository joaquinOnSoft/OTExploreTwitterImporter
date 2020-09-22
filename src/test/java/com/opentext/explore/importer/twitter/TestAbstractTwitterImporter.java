package com.opentext.explore.importer.twitter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;

import com.opentext.explore.util.FileUtil;

public class TestAbstractTwitterImporter {

	protected Properties prop = null;

	@Before
	public void setUp() {
	
		try {
			prop = new Properties();
			prop.load(FileUtil.getStreamFromResources("twitter-importer.properties"));
		} 
		catch (FileNotFoundException e) {
			System.err.println("Properties file not found");
		}
		catch (IOException e) {
			System.err.println("Properties file: " + e.getMessage());
		}		
	}

}