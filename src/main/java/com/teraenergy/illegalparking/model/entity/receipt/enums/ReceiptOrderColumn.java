package com.teraenergy.illegalparking.model.entity.receipt.enums;

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
public enum ReceiptOrderColumn {

    CAR_NAME("차량번호")
    ;

    private String value;
}
