
package com.teraenergy.illegalparking.model.entity.illegalGroup.enums;

import lombok.Getter;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
public enum GroupFilterColumn {
    NAME("그룹명"),
    LOCATION("그룹위치")
    ;

    private String value;

    GroupFilterColumn(String value){
        this.value = value;
    }

}
