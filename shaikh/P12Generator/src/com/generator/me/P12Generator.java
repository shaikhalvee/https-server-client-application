package com.generator.me;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

public class P12Generator
{
	private static final String FILE_PATH = "D:\\Important_documents\\my_codes\\Spring_Projects\\spring-boot-https-server-client\\https-example-server\\src\\main\\resources\\";
	private static final String CERTIFICATE = "public.crt";
	private static final String KEY = "private.pem";
	private static final String P12_FILE = "keystore.p12";
	private static final String PASSWORD = "123456";

	public static void main(String[] args)
	{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		try {
			Certificate certificate = CertHandler.loadCertificateFromFile(FILE_PATH + CERTIFICATE);
			PrivateKey privateKey = PrivateKeyHandler.loadKeyFromFile(FILE_PATH + KEY);
			generateP12(FILE_PATH + P12_FILE, privateKey, certificate, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void generateP12(String p12FilePath, PrivateKey privateKey, Certificate certificate, String password) throws Exception
	{
		try
		{
			Certificate[] outChain = { certificate };
			KeyStore outStore = KeyStore.getInstance("PKCS12");
			outStore.load(null, password.toCharArray());
			outStore.setKeyEntry("nexuspay", privateKey, password.toCharArray(), outChain);
			OutputStream outputStream = new FileOutputStream(p12FilePath);
			outStore.store(outputStream, password.toCharArray());
			outputStream.flush();
			outputStream.close();

			KeyStore inStore = KeyStore.getInstance("PKCS12");
			inStore.load(new FileInputStream(p12FilePath), password.toCharArray());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AssertionError(e.getMessage());
		}
	}
}
