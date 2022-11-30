package com.teraenergy.illegalparking.exception.enums;

import com.teraenergy.illegalparking.exception.TeraErrCode;
import com.teraenergy.illegalparking.exception.TeraErrCodeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-03-07
 * Author : young
 * Project : sarangbang
 * Description :
 */
@Getter
public enum TeraExceptionCode implements TeraErrCode {

    /**
     * 알 수 없는 오류
     */

    UNKNOWN("알 수 없는 오류"),
    CAST_FAILURE("cast 를 실패 하였습니다."),

    /** PRODUCE */
    PRODUCT_CREATE_FAIL("제품 생성을 실패 하였습니다."),
    PRODUCT_MODIFY_FAIL("제품 정보 수정을 실패 하였습니다."),
    PRODUCT_REMOVE_FAIL("제품 삭제를 실패 하였습니다."),

    /** REPORT */
    REPORT_OCCUR_ONE("1분 이후 접수가 필요합니다."),
    REPORT_OCCUR_FIVE("5분 이후 접수가 필요합니다."),
    REPORT_EXIST("동일 차량번호 불법주정차 신고 접수가 타인에 의해 먼저 접수되었습니다."),
    REPORT_NOT_ADD_FINISH("추가 신고가 없어 종료가 되었습니다."),
    REPORT_OVER_TIME("불법주정차 추가 신고 시간이 초과했습니다.\n 최초 신고 이후 1분 이후 10분이내 추가 신고가 되어야 합니다. "),
    REPORT_REGISTER_FAIL("신고 접수 처리가 실패하였습니다."),

    /** CAR */
    CAR_ALARM_HISTORY_GET_FAIL("내차 신고 이력 정보 가져오기를 실패 하였습니다."),
    CAR_EXIST("이미지 존재하는 차량번호입니다."),
    CAR_SET_FAIL("내차 등록이 실패 하였습니다."),


    /** ZONE & ILLEGAL PARKING */
    ZONE_CREATE_FAIL("불법주정차 구역 생성을 실패 하였습니다."),
    ZONE_MODIFY_FAIL("불법주정차 구역 수정을 실패 하였습니다."),
    ILLEGAL_PARKING_NOT_AREA("불법 주정차 대상 지역이 아닙니다."),
    ILLEGAL_PARKING_NOT_CRACKDOWN_TIME("불법 주정차 단속 시간이 아닙니다."),
    ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM("동일 차량번호 불법 주정차 신고 접수가 타인에 의해 먼저 접수 되었습니다."),

    /* USER */
    USER_WRONG_ID_PASSWORD_AGAIN_CHECK(" 사용자 정보가 잘 못 되었거나 패스워드가 틀렸습니다. \n 다시 확인하여 입력 하세요."),
    USER_INSERT_FAIL ("사용자 등록이 실패 하였습니다. \n 다시 확인하여 입력 하세요."),
    USER_FAIL_PASSWORD ("패스워드가 틀렸습니다. 다시 확인하여 입력 하세요."),
    USER_FAIL_CHANGE ("사용자 정보 변경을 실패 하였습니다."),
    USER_IS_NOT_EXIST ("존재하지 않는 사용자입니다."),
    USER_PASSWORD_IS_NOT_EXIST ("mail 또는 패스워드가 잘못 되었습니다. 확인후 다시 시도하세요."),
    USER_IS_EXIST ("이미 존재하는 사용자입니다."),
    USER_GROUP_IS_EXIST ("이미 존재하는 그룹입니다."),
    USER_FAIL_RESiSTER ("등록 실패 하였습니다."),
    USER_GET_FAIL ("사용자 정보를 가져오는데 실패 하였습니다."),

    POINT_GET_FAIL("포인트 정보를 가져오는데 실패 하였습니다."),
    NOTICE_SET_FAIL("공지사항 정보 등록을 실패 하였습니다."),
    NOTICE_GET_FAIL("공지사항 정보를 가져오는데 실패 하였습니다."),
    NOTICE_REMOVE_FAIL("공지사항 삭제를 실패 하였습니다."),
    MYPAGE_GET_FAIL("내 페이지 정보를 가져오는데 실패 하였습니다."),


    /* 파일 */
    UNSUPPORTED_FORMAT("지원되지 않는 형식입니다."),
    PARAMETER_EMPTY("%1(이)가 입력되지 않았습니다."),
    PARAMETER_INVALID("%1(이)가 잘못 입력되었습니다."),

    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_STORE_FAILURE("파일 저장 중 오류가 발생하였습니다."),
    FILE_READ_FAILURE("파일을 불러오는 중 오류가 발생하였습니다."),
    FILE_DELETE_FAILURE("파일 삭제 중 오류가 발생하였습니다."),
    DIRECTORY_MAKE_FAILURE("디렉토리 생성 중 오류가 발생하였습니다."),
    ;

    private String message;

    TeraExceptionCode(String message){
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.toString();
    }

    @Override
    public String getMessage(String... args) {
        return TeraErrCodeUtil.parseMessage(this.message, args);
    }


}
