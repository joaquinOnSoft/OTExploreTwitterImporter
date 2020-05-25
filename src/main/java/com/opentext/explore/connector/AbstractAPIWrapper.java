package com.opentext.explore.connector;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Joaquín Garzón
 */
public abstract class AbstractAPIWrapper {

	protected static final Log log = LogFactory.getLog(AbstractAPIWrapper.class);
	protected String apiKey;

	public AbstractAPIWrapper() {
	}

	/**
	 * SEE: Apache HttpClient Examples
	 * https://mkyong.com/java/apache-httpclient-examples/
	 * @param request
	 * @return
	 */
	protected String execute(HttpRequestBase request) {
		String result = null;
		CloseableHttpResponse response = null;

		CloseableHttpClient httpClient = HttpClients.createDefault();
				
		try {			
			response = httpClient.execute(request);

			// Get HttpResponse Status
			System.out.println(response.getProtocolVersion());              // HTTP/1.1
			System.out.println(response.getStatusLine().getStatusCode());   // 200
			System.out.println(response.getStatusLine().getReasonPhrase()); // OK
			System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// return it as a String
				result = EntityUtils.toString(entity);
				System.out.println(result);
			}

		} 
		catch (IOException e) {
			System.err.println(e.toString());
		}
		finally {
			try {
				if(response != null) {
					response.close();
				}
				httpClient.close();
			}
			catch(IOException e) {
				System.err.println(e.toString());				
			}
		}

		return result;
	}


	protected Object jsonStringToObject(String json, Class<?> toClass) {
		if(json == null) {
			return null;	
		}
		else {		
			// Jackson 2 – Convert Java Object to / from JSON
			// https://www.mkyong.com/java/jackson-2-convert-java-object-to-from-json/
			Object obj = null;

			ObjectMapper mapper = new ObjectMapper();
			//JSON string to Java Object
			try {
				obj = mapper.readValue(json.toString(), toClass);
			} catch (IOException e) {
				log.warn("API response (JSON Str to Object): " + e, e);
				return null;
			}
			return obj;
		}
	}

}