package com.simscale.assignment.utils;

import java.util.Random;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class RandomRequest {

	private static final Random random = new Random();
	private static final String USER_CATALOG_ELB_RESOURCE_URI = "http://apiloadbalancer.simscale.com/users/";
	
	private static final String[] names = new String[] { 	"David", "Heiny",
															"Vincenz", "Dolle",
															"Johannes", "Probst",
															"Babak", "Gholami",
															"Richard", "Szoke-Schuller",
															"Bogdan", "Daqui-Herrera",
															"Saumitra", "Joshi",
															"Anatol", "Dammer",
															"Mariano", "Barrios",
															"Aleksander", "Timic",
															"Stefan", "Milenkovic",
															"Tanmay", "Chaudhry",
															"Balsa", "Bojic",
															"Alexander", "Fischer",
															"Pawel", "Sosnowski",
															"Ali", "Arafat",
															"Ahmed", "Hussain",
															"Agata", "Krzystofik",
															"Kieran", "Hurley",
															"Anna", "Flessner",
															"Netty", "Zaharia",
															"Milad", "Mafi",
															"Marcus", "Wendland",
															"Omar", "Popal",
															"Kristina", "Lowe",
															"Sijia", "Ma",
															"Sebastian", "Gunther",
															"Guilherme", "Cardoso",}; 

	
	
	
	
	public HttpRequestBase nextRequest() {
		String randomFirstName = getRandomName();
		String randomLastName = getRandomName();
		String randomUserName = randomFirstName.toLowerCase() + "." + randomLastName.toLowerCase();
		String url = USER_CATALOG_ELB_RESOURCE_URI + randomUserName;
		
		if (random.nextFloat() <= 0.5f) {
			return new HttpGet(url);
		} else {
			String jsonBody = "{\"firstName\":\"" + randomFirstName + "\",\"lastName\":\"" + randomLastName + "\"}";

			HttpPut httpPut = new HttpPut(url);
			httpPut.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
			return httpPut;
		}
	}
	
	private String getRandomName() {
		return names[random.nextInt(names.length-1)];
	}


}
