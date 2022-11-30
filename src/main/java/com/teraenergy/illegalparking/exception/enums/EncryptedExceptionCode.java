package com.teraenergy.illegalparking.exception.enums;

import com.teraenergy.illegalparking.exception.TeraErrCode;
import com.teraenergy.illegalparking.exception.TeraErrCodeUtil;

/**
 * Date : 2022-03-14
 * Author : young
 * Project : sarangbang
 * Description :
 */
public enum EncryptedExceptionCode implements TeraErrCode {

    ENCRYPT_FAILURE("암호화 생성 실패"),

    ENCODE_FAILURE("암호화 하기 위한 코드 생성 실패"),

    DECODE_FAILURE("복호화 하기 위한 코드 생성 실패"),

    DECRYPT_FAILURE("복호화 생성 실패");


    private String message;

    EncryptedExceptionCode(String message){
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.toString();
    }

    @Override
    public String getMessage(String... args) {
        return TeraErrCodeUtil.parseMessage(this.message, args);
    }
}
