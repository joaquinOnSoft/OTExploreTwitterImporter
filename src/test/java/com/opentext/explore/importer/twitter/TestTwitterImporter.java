package com.opentext.explore.importer.twitter;



import org.junit.Test;

public class TestTwitterImporter extends TestAbstractTwitterImporter {

	@Test
	public void start() {
		TwitterImporter importer = new TwitterImporter(prop);
		importer.start();
	}
}
