package com.teraenergy.illegalparking.model.dto.illegalzone.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Date : 2022-10-10
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class PointDto {
    Integer pointSeq;       // 포인트 키
    String pointType;       // 분류
    String value;             // 제공 포인트
    String limitValue;        // 제한 포인트
    String useValue;          // 누적 사용
    String residualValue;     // 남은 포인트
    String startDate;       // 시작 일자
    String stopDate;        // 종료 일자
    String finish;      // 마감기준
    boolean isPointLimit;
    boolean isTimeLimit;
}
