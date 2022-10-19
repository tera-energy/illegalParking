package com.teraenergy.illegalparking.model.entity.mycar.service;

import com.teraenergy.illegalparking.model.entity.mycar.domain.MyCar;

import java.util.List;

/**
 * Date : 2022-10-18
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface MyCarService {

    MyCar get( Integer userSeq, String carNum);

    List<MyCar> gets(Integer userSeq);

    MyCar set(MyCar myCar);

    MyCar modify(MyCar myCar);
}