package com.simscale.assignment.loadbalancer;

public class RoundRobin extends LoadBalancer {

	public RoundRobin(String[] servers) {
		super(servers);
	}
	
	public RoundRobin(String[] servers, int timeout){
		super(servers, timeout);
	}

	private int nextServerPosition = 0;

	@Override
	protected String getNextServerHost() {
		String server = servers[nextServerPosition];

		if (nextServerPosition == servers.length - 1) {
			nextServerPosition = 0;
		} else {
			nextServerPosition++;
		}

		return server;
	}

	@Override
	protected void serverFailed(String server) {
	}

}
