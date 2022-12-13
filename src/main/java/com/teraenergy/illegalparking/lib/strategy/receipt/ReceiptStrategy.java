package com.teraenergy.illegalparking.lib.strategy.receipt;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.user.domain.User;

import java.time.LocalDateTime;

/**
 * Date : 2022-12-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface ReceiptStrategy {

    void receiptNotCode(String addr, String carNum, String fileName, LocalDateTime regDt, LawDong lawDong, User user) throws TeraException;

    void receiptNotIllegalZone(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone) throws TeraException;

    /** 신고 시간이 지난 후 신고 */
    void receiptPastTime(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone) throws TeraException;

    /** 최초신고 이후 1분 이후 10분이내 (또는 5분 이후 16분 이내) 신고 */
    void receiptAddWhenOneAndEleven(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone) throws TeraException;

    /** 불법 주정차 허용 시간에 신고 - 첫번째 허용 시간 & 두번째 허용 시간 */
    void receiptWithinAllowTime(String addr, String carNum, String fileName, LocalDateTime regDt, String regDtStr, String code, User user, IllegalZone illegalZone) throws TeraException;

    /** 허용 시간 1분 ( 또는 5분 ) 전 에 두분째 신고 */
    void receiptBeforeIllegalZoneTypeOfAllowTime(String carNum, LocalDateTime regDt, String code, Receipt receipt, User user, IllegalEvent illegalEvent) throws TeraException;

    /** 최초 신고 (중복 신고가 있는지 체크 ) */
    void receiptDuplicate(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone, IllegalEvent illegalEvent) throws TeraException;

    /** 1회 신고 ( 최초 신고 ) */
    void receiptForOCCUR(Receipt receipt) throws TeraException;

    /** 2회 신고 ( 1분 ~ 11분(또는 16분) 내에 두번째 신고 ) */
    void receiptForReport(String carNum, LocalDateTime regDt, String code, Receipt receipt, User user, IllegalEvent illegalEvent) throws TeraException;

}
