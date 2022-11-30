package com.teraenergy.illegalparking.model.dto.user.domain;

import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import lombok.Getter;
import lombok.Setter;

/**
 * Date : 2022-10-11
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Getter
@Setter
public class UserGovernmentDto {
    Integer userSeq;        // 사용자 키
    LocationType locationType;    // 지역
    String officeName;      // 관공서명
    String userName;        // 아이디
    String password;        // 패스워드
    Integer groupCount;     // 관리그룹 (개수)
    Integer totalCount;     // 신고 접수건
    Integer completeCount;    // 대기 건수 ( 현재 처리 되지 않은 건수 )
    Integer exceptionCount; // 미처리 건수 ( 과태료가 아닌 건수 )
    Integer penaltyCount;   // 처리 건수 ( 과태료 건수 )
}
