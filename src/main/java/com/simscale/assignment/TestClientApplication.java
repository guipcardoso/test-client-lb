package com.simscale.assignment;

import com.simscale.assignment.loadbalancer.LoadBalancer;
import com.simscale.assignment.loadbalancer.RoundRobin;
import com.simscale.assignment.utils.RandomRequest;

public class TestClientApplication {

	private final static long FIVE_MINUTES = 5 * 60 * 1000;

	public static void main(String[] args) {
		LoadBalancer loadBalancer = new RoundRobin(args);
		RandomRequest randomTestRequest = new RandomRequest();
		
		
		long startTime = System.currentTimeMillis();
		while (!fiveMinutesPast(startTime)) {
			loadBalancer.handleRequest(randomTestRequest.nextRequest());
		}

	}

	private static boolean fiveMinutesPast(long startTime){
		return (System.currentTimeMillis() - startTime) >= FIVE_MINUTES;
	}
}
