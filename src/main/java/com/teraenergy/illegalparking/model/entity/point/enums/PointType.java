package com.teraenergy.illegalparking.model.entity.point.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum PointType {

    PLUS("포인트제공"),

    MINUS("사용포인트")
    ;

    private String value;

    PointType(String value) {
        this.value = value;
    }

}
