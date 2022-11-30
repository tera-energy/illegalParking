package com.teraenergy.illegalparking.exception;

import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import lombok.Getter;

/**
 * Date : 2022-03-07
 * Author : young
 * Project : sarangbang
 * Description :
 */

public class TeraException extends Exception{

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;

    public TeraException(String message) {
        super(message);
        this.message = message;
    }

    public TeraException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public TeraException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public TeraException(String code, String message, Throwable e) {
        super(message, e);
        this.code = code;
        this.message = message;
    }

    public TeraException(TeraErrCode teraErrCode) {
        this(teraErrCode.getCode(), teraErrCode.getMessage(new String[0]));
    }

    public TeraException(TeraErrCode teraErrCode, Throwable e){
        this(teraErrCode.getMessage(new String[0]), e);
    }

    public TeraException(TeraErrCode teraErrCode, String... args) {
        this(teraErrCode.getCode(), teraErrCode.getMessage(args));
    }
    public TeraException(TeraErrCode teraErrCode, Throwable e, String... args) {
        this(teraErrCode.getCode(), teraErrCode.getMessage(args), e);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
