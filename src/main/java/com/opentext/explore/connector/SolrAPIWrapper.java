package com.opentext.explore.connector;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class SolrAPIWrapper extends AbstractAPIWrapper{
	private String urlBase;
	
	private static final String METHOD_OTCA_BATCH_UPDATE = "otcaBatchUpdate";

	public SolrAPIWrapper() {
		this.urlBase = "http://localhost:8983/solr/interaction/";
	}
	
	
	public SolrAPIWrapper(String urlBase) {
		this.urlBase = urlBase;
	}
	
	/**
	 * Below is the URL for the PostMan parameters.  
	 * You should be able to paste this in and the Params will populate.  
	 * Click on Body>binary and choose your file for import.  
	 * 
	 * http://localhost:8983/solr/interaction/otcaBatchUpdate?commitWithin=1000&otca.methods=languagedetector,diarisation,nsentiment,nsummarizer&otca=true&otca.diarisation.clientMapping=2&otca.diarisation.agentMapping=1

	 * @return
	 */
	public String otcaBatchUpdate(File update) {
		String response = null;
		URIBuilder builder = null;
		
		try {
			builder = new URIBuilder(urlBase + METHOD_OTCA_BATCH_UPDATE);
		} catch (URISyntaxException e) {
			System.err.println(e.getMessage());
		}
		
		if(builder != null) {
			builder.setParameter("commitWithin", "1000")
				.setParameter("otca.methods", "languagedetector,diarisation,nsentiment,nsummarizer")
				.setParameter("otca", "true")
				.setParameter("otca.diarisation.clientMapping", "2")
				.setParameter("otca.diarisation.agentMapping", "1");
			
			HttpGetWithEntity request;
			try {
				request = new HttpGetWithEntity();
				request.setURI(builder.build());
		        // add request headers
		        request.addHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8");
		        // build HttpEntity object and assign the file that need to be uploaded 
		        HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("upfile", update).build();
		        request.setEntity(entity);
		        response = execute(request);	
			} catch (URISyntaxException e) {
				System.err.println(e.getMessage());
			}
		}
		
		return response;
	}
}
