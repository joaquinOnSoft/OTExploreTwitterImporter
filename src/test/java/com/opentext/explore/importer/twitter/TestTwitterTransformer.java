package com.opentext.explore.importer.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.opentext.explore.util.DateUtil;

import twitter4j.Status;
import twitter4j.User;

public class TestTwitterTransformer {

	@Test
	public void testStatusToString() {
		List<Status> statuses = new LinkedList<Status>();
	
		Status status = mock(Status.class);
		when(status.getId()).thenReturn(1257656312529186816L);	
		when(status.getLang()).thenReturn("en");
		when(status.getText()).thenReturn("The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.");
		try {
			when(status.getCreatedAt()).thenReturn(DateUtil.utcToDate("2020-05-05T09:00:05Z"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
		User user = mock(User.class);
		when(status.getUser()).thenReturn(user);
		when(user.getName()).thenReturn("Haig Kartounian");
		when(user.getBiggerProfileImageURLHttps()).thenReturn("https://pbs.twimg.com/profile_images/602996855547408385/u1YYB6-Z.jpg");

		statuses.add(status);
		
		String xml = TwitterTransformer.statusToString(statuses);
		
		String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<add>\r\n" + 
				"  <doc>\r\n" + 
				"    <field name=\"reference_id\">1257656312529186816</field>\r\n" +
				"    <field name=\"interaction_id\">1257656312529186816</field>\r\n" +
				"    <field name=\"title\">TweetID: 1257656312529186816</field>\r\n" +
				"    <field name=\"author_name\">Haig Kartounian</field>\r\n" + 
				"    <field name=\"ID\">1257656312529186816</field>\r\n" + 
				"    <field name=\"type\">Micro Media</field>\r\n" +
				"    <field name=\"published_date\">2020-05-05T09:00:05Z</field>\r\n" + 
				"    <field name=\"date_time\">2020-05-05T09:00:05Z</field>\r\n" + 				
				"    <field name=\"content\">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>\r\n" +
				"    <field name=\"en_content\">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>\r\n" +
				"    <field name=\"profile_img\">https://pbs.twimg.com/profile_images/602996855547408385/u1YYB6-Z.jpg</field>\r\n" + 
				"  </doc>\r\n" + 
				"</add>\r\n";
		
		assertEquals(expectedXML, xml);
	}

}
;