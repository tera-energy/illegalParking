package com.teraenergy.illegalparking.lib.strategy.filter.concrete;

import com.teraenergy.illegalparking.lib.strategy.filter.FilterStrategy;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.util.CHashMap;

/**
 * Date : 2022-12-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class DefaultFilterStrategy implements FilterStrategy {

    @Override
    public Object getFilterColumn(CHashMap paramMap, Object obj) {
        String filterColumnStr = paramMap.getAsString("filterColumn");
        ReceiptFilterColumn filterColumn = (filterColumnStr == null ? ReceiptFilterColumn.ADDR : ReceiptFilterColumn.valueOf(filterColumnStr));
        return null;
    }

    @Override
    public Object getSearch(CHashMap paramMap) {
        return null;
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
