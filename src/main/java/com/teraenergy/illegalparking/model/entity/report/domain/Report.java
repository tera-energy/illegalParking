package com.teraenergy.illegalparking.model.entity.report.domain;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Integer reportSeq;

    @OneToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ReceiptSeq")
    Receipt receipt;                                            // 신고 접수건 ( 1차 / 2차 )

    @Column
    Integer reportUserSeq;                                      // 신고 처리자

    @Column
    LocalDateTime regDt = LocalDateTime.now();                  // 신고 처리 등록 시간

    @Column
    @Enumerated (EnumType.STRING)
    ReportStateType reportStateType;                            // 신고 처리 상태

    @Column
    String note;                                                // 신고 처리 내용

    @Column (nullable = false)
    Boolean isDel = false;

    @Column
    LocalDateTime delDt;

}
