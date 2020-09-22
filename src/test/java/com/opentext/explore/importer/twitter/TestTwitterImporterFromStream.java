package com.opentext.explore.importer.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestTwitterImporterFromStream extends TestAbstractTwitterImporter {
	
	@Test
	public void getUserIdByScreenName(){
		if(prop == null) {
			fail("twitter4j.properties not found");
		}
		
		String[] screenNames = {"Madrid", "LineaMadrid"};
		
		TwitterImporterFromStream importer = new TwitterImporterFromStream(prop);
		long[] ids = importer.getUserIdByScreenName(screenNames);
		
		assertNotNull(ids);
		assertEquals(2, ids.length);
		assertEquals(816178, ids[0]);
		assertEquals(197199146, ids[1]);
	}
}
