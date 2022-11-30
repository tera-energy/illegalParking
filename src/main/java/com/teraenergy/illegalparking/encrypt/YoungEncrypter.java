package com.teraenergy.illegalparking.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Date : 2022-03-14
 * Author : young
 * Project : sarangbang
 * Description :
 */
public class YoungEncrypter {
    private Cipher cipher;
    private SecretKeySpec keySpec;
    private IvParameterSpec initialVector;

    private static final String strKey = "ENCRYPT_KEY";
    private static final String strInitialVector = "ENCRYPT_IV";

    /**
     * constructor.
     *
     * Parameter is empty.
     */
    public YoungEncrypter() {
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            this.keySpec = new SecretKeySpec(md5.digest(strKey.getBytes("UTF8")), "AES");
            this.initialVector = new IvParameterSpec(md5.digest(strInitialVector.getBytes("UTF8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * constructor.
     *
     * It has Parameter for key and iv(initialize vector).
     */
    public YoungEncrypter(String strKey_, String strInitialVector_) {
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            this.keySpec = new SecretKeySpec(md5.digest(strKey_.getBytes("UTF8")), "AES");
            this.initialVector = new IvParameterSpec(md5.digest(strInitialVector_.getBytes("UTF8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts a string.
     *
     * @param value
     *        A string to encrypt. It is converted into UTF-8 before being encrypted. Null is regarded as an empty string.
     * @return An encrypted string.
     * @throws Exception
     */
    public String encrypt(String value) throws Exception{
        if (value == null)
            throw new Exception("parameter value is null");

        // Initialize the cryptography algorithm.
        this.cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.initialVector);

        // Get a UTF-8 byte array from a unicode string.
        byte[] utf8Value = value.getBytes(StandardCharsets.UTF_8);

        // Encrypt the UTF-8 byte array.
        byte[] encryptedValue = this.cipher.doFinal(utf8Value);

        return YoungEncoder.encodeByBase64(encryptedValue);
//        return Base64Encoder.encode(encryptedValue);
    }


    /**
     * Decrypts a string which is encrypted with the same key and initial vector.
     *
     * @param value
     *        A string to decrypt. It must be a string encrypted with the same key and initial vector. Null or an empty string is not allowed.
     * @return A decrypted string
     * @throws Exception
     */
    public String decrypt(String value) throws Exception {
        if (value == null || value.equals(""))
            return null;

        // Initialize the cryptography algorithm.
        this.cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.initialVector);

        // Get an encrypted byte array from a base64 encoded string.
//        byte[] encryptedValue = Base64Encoder.decode(value);
        byte[] encryptedValue = YoungEncoder.decodeByBase64(value);

        // Decrypt the byte array.
        byte[] decryptedValue = this.cipher.doFinal(encryptedValue);

        // Return a string converted from the UTF-8 byte array.
        return new String(decryptedValue, "UTF8");
    }
}
