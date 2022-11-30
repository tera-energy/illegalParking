package com.teraenergy.illegalparking.encrypt;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;

/**
 * Date : 2022-03-15
 * Author : young
 * Project : sarangbang
 * Description :
 */
public class YoungRSA {

    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;

    private String publicKeyModulus = null;
    private String publicKeyExponent = null;

    /**
     * RSA 키쌍 생성
     *
     * @param nbit
     */
    public YoungRSA(int nbit) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(nbit);

            KeyPair keyPair = keyPairGenerator.genKeyPair();

            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();

            RSAPublicKeySpec rsaPublicKeySpec = KeyFactory.getInstance("RSA").getKeySpec(publicKey, RSAPublicKeySpec.class);

            this.publicKeyModulus = rsaPublicKeySpec.getModulus().toString(16);
            this.publicKeyExponent = rsaPublicKeySpec.getPublicExponent().toString(16);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RSA 키쌍 생성
     */
    public YoungRSA() {
        this(1024);
    }

    /**
     * RSA Encrypt
     *
     * @param publicKey
     * @param value
     * @return
     */
    public static String encrypt(PublicKey publicKey, String value) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes("UTF-8"));

            return (new BigInteger(encryptedBytes)).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA Decrypt
     *
     * @param privateKey
     * @param value
     * @return
     */
    public static String decrypt(PrivateKey privateKey, String value) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedBytes = hexToByteArray(value);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 공개키 반환
     *
     * @return
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 개인키 반환
     *
     * @return
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 공개키 Modulus 반환
     *
     * @return
     */
    public String getPublicKeyModulus() {
        return publicKeyModulus;
    }

    /**
     * 공개키 Exponent 반환
     *
     * @return
     */
    public String getPublicKeyExponent() {
        return publicKeyExponent;
    }

    private static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[] {};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }


//	private String secretkey = "12345678901234567890123456789012";
//
//	private static volatile encryptModel instance;
//
//	private  encryptModel(){
//
//	}
//
//	public static encryptModel getInstance(){
//		if(instance == null){
//			synchronized(encryptModel.class){
//				if(instance == null) instance = new encryptModel();
//			}
//		}
//		return instance;
//	}
//
//	public String encrypt(String text) throws Exception{
//        Cipher cipher = Cipher.getInstance("RSA");
//        byte[] keyBytes= new byte[16];
//        byte[] b= secretkey.getBytes("UTF-8");
//        int len= b.length;
//        if (len > keyBytes.length) len = keyBytes.length;
//        System.arraycopy(b, 0, keyBytes, 0, len);
//        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
//        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
//        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
//
//        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(results);
//	}
//
//	public String decrypt(String text) throws Exception{
//        Cipher cipher = Cipher.getInstance("RSA");
//        byte[] keyBytes= new byte[16];
//        byte[] b= secretkey.getBytes("UTF-8");
//        int len= b.length;
//
//        if (len > keyBytes.length) len = keyBytes.length;
//
//        System.arraycopy(b, 0, keyBytes, 0, len);
//        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
//        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
//        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
//
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
//        return new String(results,"UTF-8");
//	}

}
