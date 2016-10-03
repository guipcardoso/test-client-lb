package com.simscale.assignment;

import com.simscale.assignment.loadbalancer.LoadBalancer;
import com.simscale.assignment.loadbalancer.RoundRobin;
import com.simscale.assignment.utils.RandomRequest;

public class TestClientApplication {

	private final static long SECONDS_IN_NANOSECONDS = 1000l * 1000 * 1000;
	private final static long FIVE_MINUTES_IN_NANOSECONDS = 5 * 60 * SECONDS_IN_NANOSECONDS;

	public static void main(String[] args) {
		LoadBalancer loadBalancer = new RoundRobin(args);
		RandomRequest randomTestRequest = new RandomRequest();
		
		
		long startTime = System.nanoTime();
		while (!fiveMinutesPast(startTime)) {
			loadBalancer.handleRequest(randomTestRequest.nextRequest());
		}

	}

	private static boolean fiveMinutesPast(long startTime){
		return (System.nanoTime() - startTime) >= FIVE_MINUTES_IN_NANOSECONDS;
	}
}
