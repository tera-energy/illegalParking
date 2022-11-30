package com.teraenergy.illegalparking.model.entity.calculate.domain;

import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
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
@Entity(name = "calculate")
public class Calculate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer calculateSeq;

    @Column (nullable = false)
    Integer userSeq;

    @Column (nullable = false)
    Long currentPointValue;

    @Column
    Long eventPointValue;

    @Column
    LocationType locationType;

    @Column
    PointType pointType;

    @Column
    String productName;

    @Column
    LocalDateTime regDt = LocalDateTime.now();

}
