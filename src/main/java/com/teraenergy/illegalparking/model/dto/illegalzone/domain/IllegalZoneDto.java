package com.teraenergy.illegalparking.model.dto.illegalzone.domain;

import com.teraenergy.illegalparking.model.entity.environment.enums.ZoneGroupType;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class IllegalZoneDto {
    Integer zoneSeq;            // 구역 키
    String polygon;             // POLYGON
    String code;                // 동 코드

    Integer eventSeq;           // 구역 키
    String name;                // 구역 이름
    String illegalType;         // 구역 타입
    Boolean usedFirst;          // 구역 첫번째 이벤트 사용 여부
    String firstStartTime;      // 구역 첫번째 시작 시간
    String firstEndTime;        // 구역 첫번째 끝 시간
    Boolean usedSecond;         // 구역 두번째 이벤트 사용 여부
    String secondStartTime;     // 구역 두번째 시작 시간
    String secondEndTime;       // 구역 두번째 끝 시간
    String zoneGroupType;       // 구역 그룹 타입
}