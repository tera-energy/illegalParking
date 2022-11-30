package com.teraenergy.illegalparking.model.entity.point.enums;

import lombok.Getter;

/**
 * Date : 2022-10-09
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
@Getter
public enum PointEventType {

    POINT_SUPPORT("포인트 제공")
    ;

    private String value;

    PointEventType(String value) {
        this.value = value;
    }
}
