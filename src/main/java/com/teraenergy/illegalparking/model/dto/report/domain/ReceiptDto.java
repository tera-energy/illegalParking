package com.teraenergy.illegalparking.model.dto.report.domain;

import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Date : 2022-10-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class ReceiptDto {
    Integer receiptSeq;                     // 신고 키
    String name;                            // 사용자 이름
    String carNum;                          // 차량 번호
    String addr;                            // 신고 주소
    Integer overlapCount;                   // 중복 횟수
    LocalDateTime regDt;                    // 신고 일자
    ReceiptStateType receiptStateType;      // 신고 상태
}
