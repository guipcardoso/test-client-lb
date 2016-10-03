package com.simscale.assignment.loadbalancer;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpRequestBase;

public abstract class LoadBalancer {

	protected String[] servers;
	
	private RequestDispatcher dispatcher;

	public LoadBalancer(String[] servers) {
		this.servers = servers;
		dispatcher = new RequestDispatcher();
	}

	public void handleRequest(HttpRequestBase request) {
		String serverHost = getNextServerHost();
		boolean forwardedSuccessfully = forwardRequest(request, serverHost);
		if (!forwardedSuccessfully) {
			serverFailed(serverHost);
			handleRequest(request);
		}

	}

	protected abstract String getNextServerHost();

	protected abstract void serverFailed(String server);

	private boolean forwardRequest(HttpRequestBase request, String serverHost) {
		try {
			URI oldURI = request.getURI();
			URI newURI = new URI(oldURI.getScheme() + "://" + serverHost + oldURI.getPath());
			request.setURI(newURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return dispatcher.dispatch(request);
	}

}
