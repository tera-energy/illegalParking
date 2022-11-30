package com.teraenergy.illegalparking.model.entity.calculate.service;

import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateFilterColumn;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateOrderColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface CalculateService {
    public Calculate get(Integer calculateSeq);

    public Calculate getAtLast(Integer userSeq);

    public List<Calculate> gets();

    public List<Calculate> getsByUser(Integer userSeq);

    public Page<Calculate> gets(int pageNumber, int pageSize, CalculateFilterColumn filterColumn, String search);

    public Calculate set(Calculate calculate);

    public List<Calculate> sets(List<Calculate> calculates);

    public Calculate modify(Calculate calculate);

    public Calculate remove(Integer calculateSeq);


}
