package com.teraenergy.illegalparking.encrypt;

import com.teraenergy.illegalparking.exception.EncryptedException;
import com.teraenergy.illegalparking.exception.enums.EncryptedExceptionCode;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * Date : 2022-03-14
 * Author : young
 * Project : sarangbang
 * Description :
 */
@Slf4j
public class YoungEncoder {

    private static final String STR_KEY = "SAMPLE_ENCRYPT_KEY";
    private static final String STR_IV = "SAMPLE_ENCRYPT_IV";

    public static String encrypt(String value) throws EncryptedException {
        try {
            return new YoungEncrypter(STR_KEY, STR_IV).encrypt(value);
        } catch (Exception e) {
            log.error("암호화 생성 실패 : ", e);
            throw new EncryptedException(EncryptedExceptionCode.ENCRYPT_FAILURE, e);
        }
    }

    public static String decrypt(String value) throws EncryptedException {
        try {
            return new YoungEncrypter(STR_KEY, STR_IV).decrypt(value);
        } catch (Exception e) {
            log.error("복호화 생성 실패 : ", e);
            throw new EncryptedException(EncryptedExceptionCode.DECRYPT_FAILURE, e);
        }
    }

    public static String encrypt(String key, String initialVector, String value) throws EncryptedException {
        try {
            return new YoungEncrypter(key, initialVector).encrypt(value);
        } catch (Exception e) {
            throw new EncryptedException(EncryptedExceptionCode.ENCRYPT_FAILURE, e);
        }
    }

    public static String decrypt(String key, String initialVector, String value) throws EncryptedException {
        try {
            return new YoungEncrypter(key, initialVector).decrypt(value);
        } catch (Exception e) {
            throw new EncryptedException(EncryptedExceptionCode.DECRYPT_FAILURE, e);
        }
    }

    public static String urlEncode(String value) throws EncryptedException {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new EncryptedException(EncryptedExceptionCode.ENCODE_FAILURE, e);
        }
    }

    public static String urlDecode(String value) throws EncryptedException {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new EncryptedException(EncryptedExceptionCode.DECODE_FAILURE, e);
        }
    }

    public static String encodeByBase64(byte[] value) throws EncryptedException {
        try {
            byte[] encoded = Base64.getEncoder().encode(value);
            return new String(encoded);
        } catch (Exception e) {
            throw new EncryptedException(EncryptedExceptionCode.ENCODE_FAILURE, e);
        }
    }

    public static byte[] decodeByBase64(String value) throws EncryptedException {
        return Base64.getDecoder().decode(value);
    }

    public static byte[] decodeByBase64(byte[] value) throws EncryptedException {
        return Base64.getDecoder().decode(value);
    }

    public static byte[] unsignedForByteArray(byte[] value) throws EncryptedException {
        try {
            byte temp[] = value;
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] < 0) {
                    temp[i] = (byte) (temp[i] + 256);
                }
            }
            return temp;
        } catch (Exception e) {
            throw new EncryptedException(EncryptedExceptionCode.DECODE_FAILURE, e);
        }
    }

}
