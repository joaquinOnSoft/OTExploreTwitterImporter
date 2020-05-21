package com.opentext.explore.importer.twitter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import twitter4j.Status;
import twitter4j.User;

public class TestTwitterTransformer {

	@Test
	public void testStatusToString() {
		List<Status> statuses = new LinkedList<Status>();
	
		Status status = mock(Status.class);
		when(status.getId()).thenReturn(1257656312529186816L);		
		when(status.getText()).thenReturn("The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.");
		User user = mock(User.class);
		when(status.getUser()).thenReturn(user);
		when(user.getName()).thenReturn("Haig Kartounian");

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
				"    <field name=\"content\">The pathway to 100% #cleanenergy continues in CA. @SCE signed agreements for 770 MW of energy storage procurement. A big step forward demonstrating that #California is continuing to invest in clean energy and grow green jobs.</field>\r\n" + 
				"  </doc>\r\n" + 
				"</add>\r\n";
		
		assertEquals(expectedXML, xml);
	}

}
;