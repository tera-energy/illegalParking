package com.teraenergy.illegalparking.lib.strategy.filter;

import com.teraenergy.illegalparking.util.CHashMap;

/**
 * Date : 2022-12-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface FilterStrategy {

    Object getFilterColumn(CHashMap paramMap, Object obj);

    Object getSearch(CHashMap paramMap);

    Object getPageNumber(CHashMap paramMap);

    Object getPageSize(CHashMap paramMap);

}
