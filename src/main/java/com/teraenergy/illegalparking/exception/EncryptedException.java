package com.teraenergy.illegalparking.exception;

import com.teraenergy.illegalparking.exception.enums.EncryptedExceptionCode;

/**
 * Date : 2022-03-14
 * Author : young
 * Project : sarangbang
 * Description :
 */
public class EncryptedException extends RuntimeException {

    public EncryptedException(EncryptedExceptionCode code) {
        super(code.getMessage());
    }

    public EncryptedException(EncryptedExceptionCode code, Exception e) {
        super(code.getMessage(), e);
    }

}
