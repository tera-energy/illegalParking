package com.teraenergy.illegalparking.exception;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface TeraErrCode {
    String getCode();

    String getMessage(String... args);
}
