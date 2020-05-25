package com.opentext.explore.connector;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * Apache HttpClient GET with body
 * SEE: https://stackoverflow.com/questions/12535016/apache-httpclient-get-with-body
 */
public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
    public final static String METHOD_NAME = "GET";

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

}
