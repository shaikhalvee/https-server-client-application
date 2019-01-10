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

public class PrivateKeyHandler
{
    public static PrivateKey loadKeyFromFile(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        KeyFactory kf2 = KeyFactory.getInstance("RSA");
        KeySpec keySpec = loadKeySpec(filePath);

//        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = (PKCS8EncodedKeySpec)keySpec;
//        ((EncodedKeySpec)pKCS8EncodedKeySpec).

        return kf2.generatePrivate(keySpec);
    }

    protected static KeySpec loadKeySpec(String filePath) throws IOException
    {
//        Object object = new PEMParser(new FileReader(new File(filePath))).readObject();
        PemReader pr1 = new PemReader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(filePath))))));
        PemObject po1 = pr1.readPemObject();
        byte[] b2 = po1.getContent();
        return new PKCS8EncodedKeySpec(b2);
    }
}
