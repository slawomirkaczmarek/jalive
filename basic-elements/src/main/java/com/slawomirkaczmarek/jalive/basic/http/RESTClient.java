package com.slawomirkaczmarek.jalive.basic.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.slawomirkaczmarek.jalive.basic.enums.HttpMethod;

public class RESTClient {
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	public String executeRequest(String url, HttpMethod method, String content) throws IOException {
		String result = null;
		HttpResponse response = null;
		switch (method) {
			case DELETE:
				response = deleteRequest(url);
				break;
			case GET:
				response = getRequest(url);
				break;
			case POST:
				response = postRequest(url, content);
				break;
			case PUT:
				response = putRequest(url, content);
				break;
		}
		if (response != null) {
			result = getResponseContent(response);
		}
		return result;
	}
	
	private HttpResponse deleteRequest(String url) throws IOException {
		GenericUrl genericUrl = new GenericUrl(url);
		HttpRequest httpRequest = HTTP_TRANSPORT.createRequestFactory().buildDeleteRequest(genericUrl);
		return httpRequest.execute();
	}
	
	private HttpResponse getRequest(String url) throws IOException {
		GenericUrl genericUrl = new GenericUrl(url);
		HttpRequest httpRequest = HTTP_TRANSPORT.createRequestFactory().buildGetRequest(genericUrl);
		return httpRequest.execute();
	}
	
	private HttpResponse postRequest(String url, String content) throws IOException {
		GenericUrl genericUrl = new GenericUrl(url);
		HttpRequest httpRequest = HTTP_TRANSPORT.createRequestFactory().buildPostRequest(genericUrl, new ByteArrayContent("application/json", content.getBytes()));
		return httpRequest.execute();
	}
	
	private HttpResponse putRequest(String url, String content) throws IOException {
		GenericUrl genericUrl = new GenericUrl(url);
		HttpRequest httpRequest = HTTP_TRANSPORT.createRequestFactory().buildPutRequest(genericUrl, new ByteArrayContent("application/json", content.getBytes()));
		return httpRequest.execute();
	}
	
	private String getResponseContent(HttpResponse response) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getContent()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
	}
}
