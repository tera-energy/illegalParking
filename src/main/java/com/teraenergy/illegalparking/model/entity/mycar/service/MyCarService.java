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

    MyCar getByAlarm( String carNum);

    MyCar get( Integer userSeq, String carNum);

    List<MyCar> gets(Integer userSeq);

    boolean isExist(String carNum);

    MyCar set(MyCar myCar);

    MyCar modify(MyCar myCar);
}
