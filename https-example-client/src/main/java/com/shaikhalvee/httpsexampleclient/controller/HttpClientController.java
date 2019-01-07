package com.shaikhalvee.httpsexampleclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// if application.properties file is added with the client-service ssl values
// then it must be called with https://localhost:8091/data/{name}
// and if application.properties file doesn't contain the ssl values {key, password, store-path}
// it can be called with http://localhost:8091/data/{name}
@RestController
public class HttpClientController {

	private static final String HTTP_SERVICE_URL = "http://localhost";
	private static final String PORT = "8099";
	private static final String API = "/goodbye/";

	// Normal HTTP call
	@GetMapping("/data/{name}")
	public String getData(@PathVariable String name) {
		RestTemplate template = new RestTemplate();
		return template.getForObject(HTTP_SERVICE_URL + ":" + PORT + API + name, String.class);
	}
}
