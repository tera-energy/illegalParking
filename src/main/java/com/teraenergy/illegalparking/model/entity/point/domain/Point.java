package com.teraenergy.illegalparking.model.entity.point.domain;

import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer pointSeq;

    @Column
    String note;

    @Column
    Long value;                                         // 제공 포인트

    @Column
    Integer groupSeq;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PointType pointType;                                // 포인트 타입

    @Column
    Long limitValue;                                    // 제한 포인트

    @Column
    LocalDate startDate;                            // 시작 일자

    @Column
    LocalDate stopDate;                             // 종료 일자

    @Column
    Boolean isPointLimit;                               // 포인트 제한 없음 여부

    @Column
    Boolean isTimeLimit;                                // 시간 제한 없음 여부

    @Column
    Long residualValue;                                 // 남은 포인트

    @Column
    Long useValue;                                      // 누적 사용 포인트

}
