package com.opentext.explore.importer.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.opentext.explore.util.DateUtil;

import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.User;

public class TestTwitterTransformer {

	private Status status;
	private User user;
	private GeoLocation geo;
	private String docXMLFragment = 
			"  <doc>\r\n" + 
			"    <field name=\"reference_id\">1257656312529186816</field>\r\n" +
			"    <field name=\"interaction_id\">1257656312529186816</field>\r\n" +
			"    <field name=\"title\">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>\r\n" +
			"    <field name=\"author_name\">Haig Kartounian</field>\r\n" + 
			"    <field name=\"ID\">1257656312529186816</field>\r\n" + 
			"    <field name=\"type\">Twitter</field>\r\n" +
			"    <field name=\"published_date\">2020-05-05T09:00:05Z</field>\r\n" + 
			"    <field name=\"date_time\">2020-05-05T09:00:05Z</field>\r\n" + 				
			"    <field name=\"content\">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>\r\n" +
			"    <field name=\"en_content\">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>\r\n" +
			"    <field name=\"profile_img\">https://pbs.twimg.com/profile_images/602996855547408385/u1YYB6-Z.jpg</field>\r\n" +
			"    <field name=\"followers\">0</field>\r\n" +
			"    <field name=\"following\">0</field>\r\n" +
			"    <field name=\"favorite_count\">0</field>\r\n" +
			"    <field name=\"retweet_count\">0</field>\r\n" +
			"    <field name=\"latitude\">40.41674</field>\r\n" +
			"    <field name=\"longitude\">-3.70303</field>\r\n" +
			"    <field name=\"itag\">Twitter Importer</field>\r\n" +
			"  </doc>\r\n"; 
	
	@Before
	public void setUp() {
		status = mock(Status.class);
		when(status.getId()).thenReturn(1257656312529186816L);	
		when(status.getLang()).thenReturn("en");
		when(status.getText()).thenReturn("The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.");
		try {
			when(status.getCreatedAt()).thenReturn(DateUtil.utcToDate("2020-05-05T09:00:05Z"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
		
		user = mock(User.class);
		when(status.getUser()).thenReturn(user);
		when(user.getName()).thenReturn("Haig Kartounian");
		when(user.getBiggerProfileImageURLHttps()).thenReturn("https://pbs.twimg.com/profile_images/602996855547408385/u1YYB6-Z.jpg");
		geo = mock(GeoLocation.class);
		when(status.getGeoLocation()).thenReturn(geo);
		when(geo.getLatitude()).thenReturn(40.416740);
		when(geo.getLongitude()).thenReturn(-3.703030);
	}
	
	@Test
	public void testStatusToString() {
		List<Status> statuses = new LinkedList<Status>();
		statuses.add(status);
		
		String xml = TwitterTransformer.statusToString(statuses, "Twitter Importer");
		
		String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<add>\r\n" +
				docXMLFragment +
				"</add>\r\n";
		
		assertEquals(expectedXML, xml);
	}
	
	@Test 
	public void testStatusToXMLFile() {
		String outputXML = "test_1257656312529186816.xml";
		
		List<Status> statuses = new LinkedList<Status>();
		statuses.add(status);
		
		try {
			TwitterTransformer.statusesToXMLFile(statuses, outputXML, "Twitter Importer");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		File xml = new File(outputXML);
		assertTrue(xml.exists());
		
		//Remove test XML
		xml.delete();
	}

}