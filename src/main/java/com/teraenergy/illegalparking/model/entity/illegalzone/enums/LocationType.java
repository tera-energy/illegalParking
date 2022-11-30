package com.teraenergy.illegalparking.model.entity.illegalzone.enums;

import lombok.Getter;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Getter
public enum LocationType {
    SEOUL("서울"),
    INCHEON("인천"),
    GYEONGGI("경기"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    DAEJEON("대전"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    BUSAN("부산"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    GWANGJU("광주"),
    JEJU("제주")
    ;

    private String value;

    LocationType(String value) {
        this.value = value;
    }
}
