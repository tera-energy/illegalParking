package com.teraenergy.illegalparking.model.entity.pm.domain;

import com.teraenergy.illegalparking.model.entity.pm.enums.PmType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Date : 2022-11-03
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "pm")
public class Pm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer pmSeq;

    @Column
    String pmId;

    @Column (nullable = false)
    String pmName;

    @Column
    String pmOperOpenHhmm;

    @Column
    String pmOperCloseHhmm;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    PmType pmType;

    @Column
    Integer pmPrice;

    @Column (nullable = false)
    Double latitude;

    @Column (nullable = false)
    Double longitude;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    Boolean isDel = false;

}
