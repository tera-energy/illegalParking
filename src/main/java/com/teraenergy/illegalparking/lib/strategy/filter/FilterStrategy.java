package com.teraenergy.illegalparking.lib.strategy.filter;

import com.teraenergy.illegalparking.util.CHashMap;

import java.lang.reflect.InvocationTargetException;

/**
 * Date : 2022-12-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface FilterStrategy {

    Object getFilterColumn(CHashMap paramMap, Class cls)throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    Object getSearch(CHashMap paramMap);

    Object getPageNumber(CHashMap paramMap);

    Object getPageSize(CHashMap paramMap);

}
