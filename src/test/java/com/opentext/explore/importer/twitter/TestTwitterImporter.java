package com.opentext.explore.importer.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.opentext.explore.util.FileUtil;

public class TestTwitterImporter {

	private Properties prop = null;
	
	@Before
	public void setUp() {

		try {
			prop = new Properties();
			prop.load(FileUtil.getStreamFromResources("twitter4j.properties"));
		} 
		catch (FileNotFoundException e) {
			System.err.println("Properties file not found");
		}
		catch (IOException e) {
			System.err.println("Properties file: " + e.getMessage());
		}		
	}
	
	@Test
	public void getUserIdByScreenName(){
		if(prop == null) {
			fail("twitter4j.properties not found");
		}
		
		String[] screenNames = {"Madrid", "LineaMadrid"};
		
		TwitterImporter importer = new TwitterImporter(prop);
		long[] ids = importer.getUserIdByScreenName(screenNames);
		
		assertNotNull(ids);
		assertEquals(2, ids.length);
		assertEquals(816178, ids[0]);
		assertEquals(197199146, ids[1]);
	}
}
