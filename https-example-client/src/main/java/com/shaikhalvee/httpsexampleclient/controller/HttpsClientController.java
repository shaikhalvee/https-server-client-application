package com.shaikhalvee.httpsexampleclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HttpsClientController {

	private static final String HTTPS_SERVICE_URL = "https://localhost";
	private static final String PORT = "8443";
	private static final String API = "/hello/";
	private static final String VALUE = "ShaikhIslamAlvee";

	// HTTPS call
	// Certificate for the required server is imported in $JAVA_HOME/lib/security/cacerts
	@GetMapping("/value")
	public String getValue() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(HTTPS_SERVICE_URL + ":" + PORT + API + VALUE, String.class);
	}
}
