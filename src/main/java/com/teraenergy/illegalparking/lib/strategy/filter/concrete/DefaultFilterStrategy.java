package com.teraenergy.illegalparking.lib.strategy.filter.concrete;

import com.teraenergy.illegalparking.lib.strategy.filter.FilterStrategy;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.util.CHashMap;
import org.apache.commons.compress.utils.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Date : 2022-12-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class DefaultFilterStrategy implements FilterStrategy {

    List<Object> objs = Lists.newArrayList();

    @Override
    public Object getFilterColumn(CHashMap paramMap, Class cls) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String filterColumnStr = paramMap.getAsString("filterColumn");
        ReceiptFilterColumn filterColumn = (filterColumnStr == null ? ReceiptFilterColumn.ADDR : ReceiptFilterColumn.valueOf(filterColumnStr));
        return null;
    }

    @Override
    public Object getSearch(CHashMap paramMap) {
        String search = paramMap.getAsString("searchStr");
        search = ( search == null ? "" : search);
        return search;
    }

    @Override
    public Object getPageNumber(CHashMap paramMap) {
        return null;
    }

    @Override
    public Object getPageSize(CHashMap paramMap) {
        return null;
    }
}
