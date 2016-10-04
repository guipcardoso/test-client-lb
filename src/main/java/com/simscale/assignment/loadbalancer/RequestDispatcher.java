package com.simscale.assignment.loadbalancer;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

public class RequestDispatcher {

	private HttpClient httpClient;

	public boolean dispatch(HttpRequestBase request) {
		try {

			int timeoutInMilliseconds = 2500;
			RequestConfig config = RequestConfig.custom().setConnectTimeout(timeoutInMilliseconds )
					.setConnectionRequestTimeout(timeoutInMilliseconds).setSocketTimeout(timeoutInMilliseconds).build();

			httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
			httpClient.execute(request);
			return true;
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return false;
	}
}