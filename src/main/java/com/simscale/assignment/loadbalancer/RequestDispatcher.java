package com.simscale.assignment.loadbalancer;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;


public class RequestDispatcher {

	private HttpClient httpClient;

	public RequestDispatcher(int timeoutInMilliseconds) {
		RequestConfig config = RequestConfig.custom()
											.setConnectTimeout(timeoutInMilliseconds)
											.setConnectionRequestTimeout(timeoutInMilliseconds)
											.setSocketTimeout(timeoutInMilliseconds)
											.build();

		httpClient = HttpClientBuilder.create()
									  .setDefaultRequestConfig(config)
									  .build();
	}

	public boolean dispatch(HttpRequestBase request) {
		try {
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