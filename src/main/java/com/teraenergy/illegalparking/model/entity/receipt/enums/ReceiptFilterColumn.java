package com.teraenergy.illegalparking.model.entity.receipt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
@AllArgsConstructor
public enum ReceiptFilterColumn {

    CAR_NUM("차량번호"),
    ADDR("위치"),
    USER("신고자")
    ;

    private String value;
}
