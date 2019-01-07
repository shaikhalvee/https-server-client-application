package com.shaikhalvee.httpsexampleclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HttpClientController {

	private static final String HTTP_SERVICE_URL = "http://localhost";
	private static final String PORT = "8099";
	private static final String API = "/goodbye/";
	private static final String VALUE = "ShaikhIslamAlvee";

	// Normal HTTP call
	@GetMapping("/data")
	public String getData() {
		RestTemplate template = new RestTemplate();
		return template.getForObject(HTTP_SERVICE_URL + ":" + PORT + API + VALUE, String.class);
	}
}
