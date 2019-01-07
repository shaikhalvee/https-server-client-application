package com.shaikhalvee.httpsexampleclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HttpsClientController {

	private static final String HTTPS_SERVICE_URL = "https://localhost:8443/hello/ShaikhIslam";
	private static final String PASSWORD = "112233";
	private static final String TRUSTSTORE_PATH = "src/main/resources/clientTrustStore.p12";
	private static final String KEY_PATH = "src/main/resources/clientKeyStore.p12";
	private static final String SERVER_CERTIFICATE_FILE = "src/main/resources/serverCert.cer";

	// Certificate for the required server is imported in $JAVA_HOME/lib/security/cacerts
	@GetMapping("/value")
	public String getValue() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(HTTPS_SERVICE_URL, String.class);
	}
}
