package com.teraenergy.illegalparking.model.dto.calculate.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class ProductDto {
    Integer productSeq;
    String name;
    String brand;
    Long pointValue;
    String userName;
    Boolean isDel;
}
