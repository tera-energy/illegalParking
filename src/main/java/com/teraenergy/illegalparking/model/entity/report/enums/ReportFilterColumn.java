
package com.teraenergy.illegalparking.model.entity.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@AllArgsConstructor
@Getter
public enum ReportFilterColumn {
    ADDR("장소"),
    ILLEGAL_TYPE("위반종류"),
    CAR_NUM("차량번호"),
    RESULT("처리결과")
    ;

    private String value;

}