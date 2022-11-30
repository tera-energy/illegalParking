package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalZoneMapperService {

    IllegalZone get(String code, double latitude, double longitude);

    IllegalZone get(Integer illegalZoneSeq);

    List<IllegalZone> getsByCode(List<String> codes, Boolean isSetting);

    List<IllegalZone> getsByGeometry(String latitude, String longitude);

    List<IllegalZone> getsByIllegalType(String illegalType);

    List<IllegalZone> getsByIllegalTypeAndCode(String illegalType, List<String> codes, Boolean isSetting);

    List<IllegalZone> gets();

    IllegalZone set(IllegalZone illegalZone);

    List<IllegalZone> sets(List<IllegalZone> illegalZones);

    void modify(IllegalZone illegalZone);

    void modifyByEvent(Integer zoneSeq, Integer eventSeq);

    void delete(Integer zoneSeq);

}
