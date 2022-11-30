package com.teraenergy.illegalparking.model.dto.report.domain;

import com.teraenergy.illegalparking.model.entity.receipt.enums.ReplyType;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Date : 2022-09-28
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class ReportDto {
    Integer reportSeq;                      // 신고 접수 키
    String name;                            // 신고자
    String carNum;                          // 차량 번호
    String addr;                            // 신고 위치
    LocalDateTime regDt;                    // 접수 일시
    ReportStateType reportStateType;        // 상태
    String governmentName;                  // 담당기관
    ReplyType replyType;                    // 처리
    String event;                           // 이벤트
    Integer overlapCount;                   // 중복 횟수
}
