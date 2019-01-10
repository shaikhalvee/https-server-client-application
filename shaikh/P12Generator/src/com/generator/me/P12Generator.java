package com.generator.me;

import java.io.*;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class P12Generator
{
    private static final String FILE_PATH = "E:\\share\\IoT Security\\shaikh\\";

    public static void main(String args[])
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Certificate certificate = null;
        try {
            certificate = CertHandler.loadCertificateFromFile(FILE_PATH + "rootCA_certificate_rsa.crt");
            PrivateKey privateKey = PrivateKeyHandler.loadKeyFromFile(FILE_PATH + "rootCA_private-key_rsa.key");
            generateP12(FILE_PATH + "rootCA_certificate_rsa.p12", privateKey, certificate, "123456");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void generateP12(String p12FilePath, PrivateKey privateKey, Certificate certificate, String password) throws Exception
    {
        try
        {
            Certificate[] outChain = { certificate };
            KeyStore outStore = KeyStore.getInstance("PKCS12");
            outStore.load(null, password.toCharArray());
            outStore.setKeyEntry("mykey", privateKey, password.toCharArray(), outChain);
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
