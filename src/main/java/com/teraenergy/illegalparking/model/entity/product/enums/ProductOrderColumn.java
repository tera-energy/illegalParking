package com.teraenergy.illegalparking.model.entity.product.enums;

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
public enum ProductOrderColumn {
    productSeq("순서"),
    brand("브랜드"),
    name("제품명"),
    point("포인트"),
    regDt("일자")
    ;

    private String value;

}
