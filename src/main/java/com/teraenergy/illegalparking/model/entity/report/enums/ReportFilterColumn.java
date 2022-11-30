
package com.teraenergy.illegalparking.model.entity.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
public enum ReportFilterColumn {
    CAR_NUM("차량번호"),
    ADDR("위치"),
    USER("신고자")
    ;

    private String value;

    ReportFilterColumn(String value){
        this.value = value;
    }

}
