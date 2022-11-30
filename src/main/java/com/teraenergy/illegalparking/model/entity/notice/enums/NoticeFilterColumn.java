package com.teraenergy.illegalparking.model.entity.notice.enums;

import lombok.Getter;

/**
 * Date : 2022-09-19
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
public enum NoticeFilterColumn {
    SUBJECT("제목"),
    CONTENT("내용"),
    NOTICETYPE("분류")
    ;

    private String value;

    NoticeFilterColumn(String value) {
        this.value = value;
    }

}
