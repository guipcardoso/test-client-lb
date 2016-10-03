package com.simscale.assignment.loadbalancer;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

public class RequestDispatcher {

	private HttpClient httpClient;

	public boolean dispatch(HttpRequestBase request) {
		try {
			httpClient = HttpClientBuilder.create().build();
			httpClient.execute(request);
			return true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}