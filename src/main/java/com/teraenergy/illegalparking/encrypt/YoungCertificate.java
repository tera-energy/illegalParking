package com.teraenergy.illegalparking.encrypt;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Date : 2022-03-15
 * Author : young
 * Project : sarangbang
 * Description :
 */
public class YoungCertificate {


    public static X509Certificate getX509Certificate(File file) throws Exception{
        X509Certificate cert = null;
        FileInputStream fis = null;
        try {
//            fis = new FileInputStream(new File("C:/signCert.der"));
            fis = new FileInputStream(file);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            cert = (X509Certificate) certificateFactory.generateCertificate(fis);
        } catch (Exception e) {
            if (fis != null) try { fis.close(); } catch(Exception ie) {}
            throw new Exception(e);
        }

        return cert;
    }

    public static EncryptedPrivateKeyInfo getPrivateKey(File file) throws Exception{

        byte[] encodedKey = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
//            fis = new FileInputStream(new File("C:/signPri.key"));
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            encodedKey = bos.toByteArray();
        } catch (Exception e) {
            if (bos != null) try {bos.close();} catch(Exception ie) {}
            if (fis != null) try {fis.close();} catch(Exception ie) {}
            throw new Exception(e);
        }

        // 2. 개인카 파일 분석하기
        EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(encodedKey);
        return encryptedPrivateKeyInfo;

    }

}
