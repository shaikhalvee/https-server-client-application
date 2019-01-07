package com.shaikhalvee.httpsexampleclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HttpsURLConnection;

@SpringBootApplication
public class HttpsExampleClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpsExampleClientApplication.class, args);
	}

}

