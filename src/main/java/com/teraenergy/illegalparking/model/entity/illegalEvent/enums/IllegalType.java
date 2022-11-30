package com.teraenergy.illegalparking.model.entity.illegalEvent.enums;

import com.teraenergy.illegalparking.model.entity.illegalzone.handler.IllegalTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

/**
 * Date : 2022-09-23
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum IllegalType {

    ILLEGAL("불법주정차"),
    FIVE_MINUTE("5분주정차");

    private String value;

    IllegalType(String value) {
        this.value = value;
    }

    @MappedTypes(IllegalType.class)
    public static class TypeHandler extends IllegalTypeHandler<IllegalType> {
        public TypeHandler() {
            super(IllegalType.class);
        }
    }

}
