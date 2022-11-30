package com.teraenergy.illegalparking.model.entity.parking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-19
 * Author : young
 * Project : illegalParking
 * Description :
 */

@AllArgsConstructor
@Getter
public enum ParkingFilterColumn {
    prkplceNm("주차장명"),
    parkingchrgeInfo("요금")
    ;

    private String value;

}
