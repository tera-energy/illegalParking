package com.teraenergy.illegalparking.model.entity.pm.enums;

import lombok.Getter;

/**
 * Date : 2022-11-03
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum PmType {

    BIKE("자전거"),

    KICK("킥보드")
    ;

    String value;

    PmType(String value) {
        this.value = value;
    }
}
