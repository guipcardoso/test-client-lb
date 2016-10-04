package com.simscale.assignment.loadbalancer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(RoundRobin.class)
public class RoundRobinTest {

	private RoundRobin roundRobin;
	private RequestDispatcher mockedDispatcher = mock(RequestDispatcher.class);
	
	@Captor
	private ArgumentCaptor<HttpRequestBase> requestCaptor;
	
	private static final String FIRST_SERVER_HOST  = "1.1.1.1:8080";
	private static final String SECOND_SERVER_HOST = "2.2.2.2:8080";
	private static final String THIRD_SERVER_HOST  = "3.3.3.3:8080";

	@Before
	public void setup() throws Exception {
		PowerMockito.whenNew(RequestDispatcher.class).withNoArguments().thenReturn(mockedDispatcher);
		roundRobin = new RoundRobin(new String[] { 	FIRST_SERVER_HOST,
													SECOND_SERVER_HOST,
													THIRD_SERVER_HOST  });
}

	@Test
	public void requestShouldBeSentToFirstServer(){
		//given
		HttpGet httpGetRequest = new HttpGet("http://apiloadbalancer.simscale.com/users/john.doe");
		when(mockedDispatcher.dispatch(httpGetRequest)).thenReturn(true);
		
		//when
		roundRobin.handleRequest(httpGetRequest);
		
		//then
		verify(mockedDispatcher, times(1)).dispatch(requestCaptor.capture());
		HttpRequestBase requestSent = requestCaptor.getValue();
		
		assertRequestWasSentToExpectedServer(httpGetRequest, requestSent, FIRST_SERVER_HOST);
	}
	

	@Test
	public void requestShouldBeSentToSecondServerIfFirstOneFails(){
		//given
		HttpGet httpGetRequest = new HttpGet("http://apiloadbalancer.simscale.com/users/john.doe");
		when(mockedDispatcher.dispatch(httpGetRequest)).thenReturn(false).thenReturn(true);
		
		//when
		roundRobin.handleRequest(httpGetRequest);
		
		//then
		verify(mockedDispatcher, times(2)).dispatch(requestCaptor.capture());
		HttpRequestBase requestSent = requestCaptor.getValue();
		
		assertRequestWasSentToExpectedServer(httpGetRequest, requestSent, SECOND_SERVER_HOST);
	}
	
	@Test
	public void requestsShouldBeSentToAllServersBackToFirst(){
		//given
		HttpGet httpGetRequest1 = new HttpGet("http://apiloadbalancer.simscale.com/users/john.doe");
		HttpGet httpGetRequest2 = new HttpGet("http://apiloadbalancer.simscale.com/users/jane.doe");
		HttpGet httpGetRequest3 = new HttpGet("http://apiloadbalancer.simscale.com/users/guilherme.cardoso");
		HttpGet httpGetRequest4 = new HttpGet("http://apiloadbalancer.simscale.com/users/wilhelm.cardoso");
		
		when(mockedDispatcher.dispatch(httpGetRequest1)).thenReturn(true);
		when(mockedDispatcher.dispatch(httpGetRequest2)).thenReturn(true);
		when(mockedDispatcher.dispatch(httpGetRequest3)).thenReturn(true);
		when(mockedDispatcher.dispatch(httpGetRequest4)).thenReturn(true);
		
		//when
		roundRobin.handleRequest(httpGetRequest1);
		roundRobin.handleRequest(httpGetRequest2);
		roundRobin.handleRequest(httpGetRequest3);
		roundRobin.handleRequest(httpGetRequest4);
		
		//then
		verify(mockedDispatcher, times(4)).dispatch(requestCaptor.capture());
		List<HttpRequestBase> requestsSent = requestCaptor.getAllValues();

		assertRequestWasSentToExpectedServer(httpGetRequest1, requestsSent.get(0), FIRST_SERVER_HOST);
		assertRequestWasSentToExpectedServer(httpGetRequest2, requestsSent.get(1), SECOND_SERVER_HOST);
		assertRequestWasSentToExpectedServer(httpGetRequest3, requestsSent.get(2), THIRD_SERVER_HOST);
		assertRequestWasSentToExpectedServer(httpGetRequest4, requestsSent.get(3), FIRST_SERVER_HOST);
	}
	
	private void assertRequestWasSentToExpectedServer(HttpRequestBase requestSent, HttpRequestBase capturedRequest, String serverHost){
		assertEquals(buildExpectedURI(requestSent.getURI(), serverHost), capturedRequest.getURI().toString());
	}
	
	private String buildExpectedURI(URI oldURI, String newHost){
		return oldURI.getScheme() + "://" + newHost + oldURI.getPath();
	}
	
}
