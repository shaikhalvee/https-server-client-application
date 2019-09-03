package com.generator.me;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

class PrivateKeyHandler
{
	static PrivateKey loadKeyFromFile(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec pkcs8KeySpec = loadKeySpec(filePath);

//        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = (PKCS8EncodedKeySpec)pkcs8KeySpec;
//        ((EncodedKeySpec)pKCS8EncodedKeySpec).

		return keyFactory.generatePrivate(pkcs8KeySpec);
	}

	private static KeySpec loadKeySpec(String filePath) throws IOException
	{
//        Object object = new PEMParser(new FileReader(new File(filePath))).readObject();
		PemReader pemReader = new PemReader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(filePath))))));
		PemObject pemObject = pemReader.readPemObject();
		byte[] pemObjectContent = pemObject.getContent();
		return new PKCS8EncodedKeySpec(pemObjectContent);
	}
}
