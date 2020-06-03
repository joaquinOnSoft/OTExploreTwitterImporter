package com.opentext.explore.connector;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SolrAPIWrapper extends AbstractAPIWrapper{
	private String urlBase;
	
	protected static final Logger log = LogManager.getLogger(SolrAPIWrapper.class);

	private static final String METHOD_OTCA_BATCH_UPDATE = "/solr/interaction/otcaBatchUpdate";

	public SolrAPIWrapper() {
		this.urlBase = "http://localhost:8983";
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
			log.error(e.getMessage());
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
		        //request.addHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8");
				request.addHeader(HttpHeaders.ACCEPT, "*.*");
		        request.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
		        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
		        request.addHeader(HttpHeaders.CONNECTION, "keep-alive");
		        // build HttpEntity object and assign the file that need to be uploaded 
		        
		        HttpEntity entity = EntityBuilder.create()
		        		.setBinary(Files.readAllBytes(update.toPath()))
		        		.build();
		        request.setEntity(entity);
		        
		        response = execute(request);	
			} catch (URISyntaxException | IOException e) {
				log.error(e.getMessage());
			}
		}
		
		return response;
	}
}
