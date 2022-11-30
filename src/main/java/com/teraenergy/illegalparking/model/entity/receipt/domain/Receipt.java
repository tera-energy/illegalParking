package com.teraenergy.illegalparking.model.entity.receipt.domain;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReplyType;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
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
@Entity(name = "receipt")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer receiptSeq;

    @Column
    LocalDateTime regDt;

    @Column
    LocalDateTime secondRegDt;

    @Column
    String carNum;

    @Column(nullable = false)
    Boolean isDel = false;

    @Column
    LocalDateTime delDt;

    @Column
    String fileName;

    @Column
    String secondFileName;

    @Column
    String addr;

    @Column
    String code;

    @Column
    @Enumerated(EnumType.STRING)
    ReplyType replyType;

    @Column
    @Enumerated(EnumType.STRING)
    ReceiptStateType receiptStateType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zoneSeq")
    IllegalZone illegalZone;

}
