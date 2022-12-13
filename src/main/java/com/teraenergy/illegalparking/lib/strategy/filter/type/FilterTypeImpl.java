package com.teraenergy.illegalparking.lib.strategy.filter.type;

import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * Date : 2022-12-13
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class FilterTypeImpl implements FilterType{

    List<Object> objectList = Lists.newArrayList();

    @Override
    public Object getFilterType(Class cls) {
        for( Object obj : objectList) {
            if (obj.getClass() == cls.getClass()) {
                return obj;
            }
        }
        return null;
    }
}
