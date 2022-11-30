package com.teraenergy.illegalparking.model.entity.product.domain;

import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.product.enums.Brand;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer productSeq;

    @Column
    String name;

    @Column
    @Enumerated(EnumType.STRING)
    Brand brand;

    @Column (nullable = false)
    long pointValue;

    @Column
    Integer userSeq;

    @Column
    String thumbnail;

    @Column (nullable = false)
    LocalDateTime RegDt = LocalDateTime.now();

    @Column (nullable = false)
    Boolean isDel = false;

}
