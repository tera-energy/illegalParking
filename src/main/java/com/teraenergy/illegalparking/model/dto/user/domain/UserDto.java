package com.teraenergy.illegalparking.model.dto.user.domain;

import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Getter
@Setter
public class UserDto {
    Integer userSeq;
    private String userName;    // 사용자 ID
    private String name;        // 사용자 이름
    private Role role;          // 사용자 권한
    private Long userCode;      // 사용자 분리 코드
    private String photoName;       // 사진 이름 (모바일 전용)
    private String phoneNumber;     // 전화 번호 (모바일 전용)
}
