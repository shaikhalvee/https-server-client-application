package com.generator.me;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class CertHandler
{
    public static Certificate loadCertificateFromFile(String certificatePath) throws FileNotFoundException, CertificateException
    {
        FileInputStream fis = new FileInputStream(certificatePath);
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(bis);
    }
}
