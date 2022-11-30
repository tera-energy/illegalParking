package com.teraenergy.illegalparking.model.entity.receipt.enums;

import lombok.Getter;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Getter
public enum ReplyType {


    ILLEGAL_ZONE_NOT_AREA("불법주정차 대상지역이 아닙니다."),
    REPORT_EXCEPTION("불법주정차 과태료 대상 접수되었지만 최종 신고에서 제외되었습니다."),
    REPORT_COMPLETE("불버주정차 신고가 접수완료되어 해당 부서에 전송 되었습니다."),
    REPORT_AS_SOON_AS("1분 이후 추가 접수가 필요합니다."),
    REPORT_EXIT("추가 신고가 없어 종료되었습니다."),
    REPORT_EXIST("동일 차량번호 불법주정차 신고 접수가 타인에 의해 먼저 접수 되었습니다."),
    TIME_NOT("불법주정차 단속 시간이 아닙니다."),
    TIME_OVER_SUBSCRITION("최초신고 이후 1분 이후 10분이내 추가 신고가 되어야 합니다."),
    TIME_OVER("불법주정차 추가 신고 시간이 초과했습니다."),
    GIVE_PENALTY("해당 불법주정차 차량에 과태료가 부가되었습니다.")
    ;

    private String value;

    ReplyType(String value) {
        this.value = value;
    }

}
