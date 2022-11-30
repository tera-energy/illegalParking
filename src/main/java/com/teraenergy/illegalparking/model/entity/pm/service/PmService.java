package com.teraenergy.illegalparking.model.entity.pm.service;

import com.teraenergy.illegalparking.model.entity.pm.domain.Pm;

import java.util.List;

/**
 * Date : 2022-11-03
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface PmService {

    Pm get(Integer pmSeq);

    List<Pm> gets();

    List<Pm> gets(List<String> codes);

    Pm set(Pm pm);

    List<Pm> sets(List<Pm> pms);
}
