package com.shaikhalvee.httpsexampleclient.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@RestController
public class HttpsClientController {

	// api configuration
	private static final String HTTPS_SERVICE_URL = "https://localhost";
	private static final String PORT = "8443";
	private static final String API = "/hello/";

	// key & certificate configuration
	private static final String PASSWORD = "123456";
	private static final String TRUSTSTORE_PATH = "src/main/resources/truststore.p12";
	private static final String KEY_PATH = "src/main/resources/clientKeyStore.p12";
	private static final String SERVER_CERTIFICATE_FILE = "src/main/resources/serverCert.cer";


	// HTTPS call
	// Certificate for the required server is imported in $JAVA_HOME/lib/security/cacerts
	@GetMapping("/value/{name}")
	public String getValue(@PathVariable String name) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(HTTPS_SERVICE_URL + ":" + PORT + API + name, String.class);
	}


	// HTTPS call
	// If Server Certificate file is not added, then it is stored in trust file{key store}
	// SSLSocket is created using client key store & trust store
	// Then HTTPS call is made using this socket. Implemented below
	@GetMapping("/value/key-trust/{name}")
	public String getValueWithKeyTrust(@PathVariable String name) {
		RestTemplate restTemplate = getRestTemplateWithKeyAndTrustStore();
		return restTemplate.getForObject(HTTPS_SERVICE_URL + ":" + PORT + API + name, String.class);
	}

	// HTTPS call
	// using only trust store
	@GetMapping("/value/trust/{name}")
	public String getValueWithOnlyTrust(@PathVariable String name) {
		RestTemplate restTemplate = getRestTemplateWithOnlyTrustStore();
		return restTemplate.getForObject(HTTPS_SERVICE_URL + ":" + PORT + API + name, String.class);
	}


	private RestTemplate getRestTemplateWithKeyAndTrustStore() {
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
		} catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException | IOException e) {
			// Better to use Exception, But only using it because to identify which
			// type of exception can occur
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
			KeyStore.TrustedCertificateEntry certificateEntry = new KeyStore.TrustedCertificateEntry(certificate);

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
