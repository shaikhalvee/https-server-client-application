package com.shaikhalvee.httpsexampleclient.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@RestController
public class HttpsExampleClientController {

	private static final String SERVICE_URL = "https://localhost:8443/hello/ShaikhIslam";
	private static final String PASSWORD = "112233";
	private static final String TRUSTSTORE_PATH = "src/main/resources/clientTrustStore.p12";
	private static final String KEY_PATH = "src/main/resources/clientKeyStore.p12";
	private static final String SERVER_CERTIFICATE_FILE = "src/main/resources/serverCert.cer";

	@GetMapping("/data")
	public String getData() {
//		RestTemplate restTemplate = new RestTemplate();
		RestTemplate restTemplate = getRestTemplateWithKeyAndTrustStore();
//		RestTemplate restTemplate = getRestTemplateWithOnlyTrustStore();
//		RestTemplate restTemplate = getRestTemplateWithOnlyCertificate();
//		RequestEntity<String> requestEntity = new RequestEntity<String>(String.class);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(SERVICE_URL, String.class);

		return responseEntity.getStatusCode().toString() + "::::" + responseEntity.getBody();
	}

	@GetMapping("/value")
	public String getValue() {
//		File certificateFile = new File(SERVER_CERTIFICATE_FILE);
		try {
//			InputStream certificateInputStream = new FileInputStream(certificateFile);
//			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//			X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);

			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(SERVICE_URL, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Value not found";
	}

	private RestTemplate getRestTemplateWithKeyAndTrustStore(){
		RestTemplate restTemplate = null;
		File certificateFile = new File(TRUSTSTORE_PATH);
		File keyFile = new File(KEY_PATH);

		try {
			InputStream keyInputStream = new FileInputStream(keyFile);
			InputStream certificateInputStream = new FileInputStream(certificateFile);

			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(keyInputStream, PASSWORD.toCharArray());

			KeyStore trustedStore = KeyStore.getInstance("PKCS12");
			trustedStore.load(certificateInputStream, PASSWORD.toCharArray());

			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
					new SSLContextBuilder()
					.loadTrustMaterial(trustedStore, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keyStore, PASSWORD.toCharArray())
					.build()
			);

			HttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(socketFactory)
					.build();
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			restTemplate = new RestTemplate(requestFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restTemplate;
	}

	private RestTemplate getRestTemplateWithOnlyTrustStore(){
		RestTemplate restTemplate = null;
		File certificateFile = new File(TRUSTSTORE_PATH);

		try {
			InputStream certificateInputStream = new FileInputStream(certificateFile);

			KeyStore trustedStore = KeyStore.getInstance("PKCS12");
			trustedStore.load(certificateInputStream, PASSWORD.toCharArray());

			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
					new SSLContextBuilder()
							.loadTrustMaterial(trustedStore, new TrustSelfSignedStrategy())
							.build()
			);

			HttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(socketFactory)
					.build();
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			restTemplate = new RestTemplate(requestFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restTemplate;
	}

	// can't help with it, only process is to import cert file to $JAVA_HOME/bin/security/cacerts
	private RestTemplate getRestTemplateWithOnlyCertificate() {
		RestTemplate restTemplate = null;
		File certificateFile = new File(SERVER_CERTIFICATE_FILE);

		try {
			InputStream certificateInputStream = new FileInputStream(certificateFile);
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);
			TrustedCertificateEntry certificateEntry = new TrustedCertificateEntry(certificate);

			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(null, null);

			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
					new SSLContextBuilder()
							.build()
			);

			HttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(socketFactory)
					.build();
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			restTemplate = new RestTemplate(requestFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restTemplate;
	}
}
