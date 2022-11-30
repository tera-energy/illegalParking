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
public enum ProductFilterColumn {
    name("상품명"),
    brand("브랜드"),
    point("포인트")
    ;

    private String value;

}
