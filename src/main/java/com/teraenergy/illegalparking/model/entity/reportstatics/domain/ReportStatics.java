package com.teraenergy.illegalparking.model.entity.reportstatics.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Date : 2022-10-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity (name = "report_statics")
public class ReportStatics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer reportStaticsSeq;

    @Column (nullable = false)
    String code;

    @Column (nullable = false)
    Integer receiptCount = 0;

    @Column (nullable = false)
    Integer zoneSeq;

}
