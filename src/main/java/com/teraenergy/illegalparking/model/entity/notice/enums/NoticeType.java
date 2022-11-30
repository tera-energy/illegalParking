package com.teraenergy.illegalparking.model.entity.notice.enums;

import lombok.Getter;

/**
 * Date : 2022-10-18
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Getter
public enum NoticeType {

    DISTRIBUTION("공지"),
    ANNOUNCEMENT("소식")
    ;


    String value;

    NoticeType(String value){
        this.value = value;
    }

}
