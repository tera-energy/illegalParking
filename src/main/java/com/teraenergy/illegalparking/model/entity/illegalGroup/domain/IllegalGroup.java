package com.teraenergy.illegalparking.model.entity.illegalGroup.domain;

import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Setter
@Getter
@Entity(name = "illegal_group")
public class IllegalGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Integer groupSeq;

    @Column (nullable = false)
    String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    LocationType locationType;

    @Column (nullable = false)
    Boolean isDel = false;

    @Column
    LocalDateTime delDt;

}
