package com.teraenergy.illegalparking.model.entity.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-03-07
 * Author : young
 * Project : sarangbang
 * Description :
 */
@AllArgsConstructor
@Getter
public enum Role {

    USER("ROLE_USER"),

    GOVERNMENT("ROLE_GOVERNMENT"),

    ADMIN("ROLE_ADMIN");


    private String value;

}
