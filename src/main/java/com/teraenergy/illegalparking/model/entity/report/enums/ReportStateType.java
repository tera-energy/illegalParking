package com.teraenergy.illegalparking.model.entity.report.enums;

import lombok.Getter;

/**
 * Date : 2022-09-29
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum ReportStateType {
    COMPLETE("신고접수"),
    EXCEPTION("신고제외"),
    PENALTY("과태료대상")
        ;

    private String value;

    ReportStateType(String value) {
        this.value = value;
    }

}
