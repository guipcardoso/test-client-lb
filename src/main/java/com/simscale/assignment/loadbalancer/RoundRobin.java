package com.simscale.assignment.loadbalancer;

import java.util.HashMap;
import java.util.Map;

import com.simscale.assignment.utils.Logger;

public class RoundRobin extends LoadBalancer {

	private static long THIRTY_SECONDS = 30 * 1000;

	public RoundRobin(String[] servers) {
		super(servers);
	}

	private Map<String, Long> recentlyFailedServers = new HashMap<String, Long>();

	private int nextServerPosition = 0;

	@Override
	protected String getNextServerHost() {
		if (!isAnyServerAvailable()) throw new RuntimeException("No servers available.");
		
		String server = servers[nextServerPosition];

	    if (nextServerPosition == (servers.length - 1)) {
	    	nextServerPosition = 0; 
	    } else {
	    	nextServerPosition++;
	    }
	    
		if (hasServerFailedRecently(server)) {
			if (hasTimeUntilRetryPassed(server, THIRTY_SECONDS)) {
				recentlyFailedServers.remove(server);
			} else {
				return getNextServerHost();
			}
		}
		
		return server;
	}

	@Override
	protected void serverFailed(String server) {
		recentlyFailedServers.put(server, System.currentTimeMillis());
		Logger.log("Server host " + server + " failed. Request will be sent to next server");
	}

	private boolean hasServerFailedRecently(String server) {
		return recentlyFailedServers.containsKey(server);
	}

	private boolean hasTimeUntilRetryPassed(String server, long timeUntilRetry) {
		return (System.currentTimeMillis() - recentlyFailedServers.get(server)) >= timeUntilRetry;
	}
	
	private boolean isAnyServerAvailable(){
		return recentlyFailedServers.size() < servers.length;
	}

}
