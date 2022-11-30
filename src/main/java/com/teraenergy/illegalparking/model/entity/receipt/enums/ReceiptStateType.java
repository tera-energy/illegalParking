package com.teraenergy.illegalparking.model.entity.receipt.enums;

import lombok.Getter;

/**
 * Date : 2022-09-25
 * Author : young
 * Project : illegalParking
 * Description :
 */
@Getter
public enum ReceiptStateType {

    /**
     * 1. 최초 신고 상태 : 신고 발생
     * 2. 두번째 신고 상태 : 신고 접수  or 신고 누락 -> 신고 발생으로 등록
     * 3. 결과 : 과태료 대상 or 신고 제외
     *
     * 신고 대기(1), 신고 불가(2), 신고 종료(3), 신고 접수(4),  신고 제외(5), 과태료 대상(6),
     * 신고발생 -> 신고대기
     * 신고누락 -> 신고종료
     * 신고불가는 추가
     * */


    OCCUR("신고대기"),
    EXCEPTION("신고제외"),
    FORGET("신고종료"),
    NOTHING("신고불가"),
    REPORT("신고접수"),
    PENALTY("과태료 대상")
    ;

    private String value;

    ReceiptStateType(String value) {
        this.value = value;
    }

}
