package com.teraenergy.illegalparking.model.entity.parking.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity (name = "parking")
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer parkingSeq;

    @Column
    String prkplceNo;

    @Column
    String prkplceNm;

    @Column
    String prkplceSe;

    @Column
    String prkplceType;

    @Column
    String rdnmadr;

    @Column
    String lnmadr;

    @Column
    Integer prkcmprt;

    @Column
    Integer feedingSe;

    @Column
    String enforceSe;

    @Column
    String operDay;

    @Column
    String weekdayOperOpenHhmm;

    @Column
    String weekdayOperColseHhmm;

    @Column
    String satOperOpenHhmm;

    @Column
    String satOperCloseHhmm;

    @Column
    String holidayOperOpenHhmm;

    @Column
    String holidayOperCloseHhmm;

    @Column
    String parkingchrgeInfo;

    @Column
    String basicTime;

    @Column
    Integer basicCharge;

    @Column
    String addUnitTime;

    @Column
    Integer addUnitCharge;

    @Column
    String dayCmmtktAdjTime;

    @Column
    Integer dayCmmtkt;

    @Column
    Integer monthCmmtkt;

    @Column
    String metpay;

    @Column
    String spcmnt;

    @Column
    String institutionNm;

    @Column
    String phoneNumber;

    @Column
    Double latitude;

    @Column
    Double longitude;

    @Column
    LocalDate referenceDate;

    @Column(nullable = false)
    Boolean isDel;

    @Column(nullable = false)
    String code;

}
