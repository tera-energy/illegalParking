package com.teraenergy.illegalparking.model.dto.user.enums;

import lombok.Getter;

/**
 * Date : 2022-10-11
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum UserGovernmentFilterColumn {

    OFFICE_NAME("관공서명"),
    LOCATION ("지역")
    ;

    private String value;

    UserGovernmentFilterColumn(String value) {
        this.value = value;
    }
}
