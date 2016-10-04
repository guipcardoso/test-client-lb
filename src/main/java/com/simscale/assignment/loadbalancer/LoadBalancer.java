package com.simscale.assignment.loadbalancer;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import org.apache.http.client.methods.HttpRequestBase;

import com.simscale.assignment.utils.Logger;

public abstract class LoadBalancer {

	protected String[] servers;
	
	private RequestDispatcher dispatcher;

	public LoadBalancer(String[] servers) {
		this.servers = servers;
		dispatcher = new RequestDispatcher();
	}

	public void handleRequest(HttpRequestBase request) {
		String serverHost = getNextServerHost();
		long startTime = System.currentTimeMillis();
		boolean forwardedSuccessfully = forwardRequest(request, serverHost);
		long responseTime = System.currentTimeMillis() - startTime;
		if (!forwardedSuccessfully) {
			serverFailed(serverHost);
			handleRequest(request);
		} else {
			Logger.log(requestSentLogMessage(request, serverHost, responseTime));
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

	private String requestSentLogMessage(HttpRequestBase request, String serverHost, long responseTime) {
		return MessageFormat.format("{0} {1} - response time: {3}ms",
									request.getMethod(),
									String.format("%1$-" + 55 + "s", request.getURI().toString()), //padding
									serverHost, responseTime);
	}
}
