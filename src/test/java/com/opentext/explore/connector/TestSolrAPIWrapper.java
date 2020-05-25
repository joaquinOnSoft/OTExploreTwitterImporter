package com.opentext.explore.connector;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Test;


/**
 * SEE: Mocking Apache HTTPClient using Mockito 
 * https://stackoverflow.com/questions/20542361/mocking-apache-httpclient-using-mockito
 * 
 * SEE: Mocking static methods with Mockito
 * https://stackoverflow.com/questions/21105403/mocking-static-methods-with-mockito
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(HttpClients.class)
public class TestSolrAPIWrapper {
	@Test
	public void testOtcaBatchUpdate() {
		File file = new File(
				getClass().getClassLoader().getResource("1257656312529186816.xml").getFile()
				);		

		//given:
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		
		StatusLine statusLine = mock(StatusLine.class);
		
		CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
		
		HttpGetWithEntity httpGet = mock(HttpGetWithEntity.class);

		CloseableHttpClient httpClient = mock(CloseableHttpClient.class);

		//PowerMockito.mockStatic(HttpClients.class);
	
		//and:
		//Mockito.when(HttpClients.createDefault()).thenReturn(httpClient);
		
		try {
			when(httpClient.execute(httpGet)).thenReturn(httpResponse);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}

		when(statusLine.getProtocolVersion()).thenReturn(protocol);
		when(statusLine.getStatusCode()).thenReturn(200);
		when(statusLine.getReasonPhrase()).thenReturn("OK");     
		when(statusLine.toString()).thenReturn("HTTP/1.1 200 OK");

		when(httpResponse.getStatusLine()).thenReturn(statusLine);

		try {
			when(httpClient.execute(httpGet)).thenReturn(httpResponse);
		} catch (ClientProtocolException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

		SolrAPIWrapper wrapper = new SolrAPIWrapper();
		String response = wrapper.otcaBatchUpdate(file);

		String expectedResponse = ""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<response>\r\n" + 
				"    <lst name=\"responseHeader\">\r\n" + 
				"        <arr name=\"errors\"/>\r\n" + 
				"        <int name=\"maxErrors\">-1</int>\r\n" + 
				"        <int name=\"status\">0</int>\r\n" + 
				"        <int name=\"QTime\">91</int>\r\n" + 
				"    </lst>\r\n" + 
				"    <lst name=\"exploredocfields\">\r\n" + 
				"        <lst name=\"1257656312529186816\"/></lst>\r\n" +  
				"</response>";
		//then:
		Assert.assertNotNull(response);
		Assert.assertEquals(expectedResponse, response);
	}
}
