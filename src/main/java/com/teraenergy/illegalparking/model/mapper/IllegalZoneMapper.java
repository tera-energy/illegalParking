package com.teraenergy.illegalparking.model.mapper;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Mapper
public interface IllegalZoneMapper {

    IllegalZone findByLatitudeAndLongitude(String code, double latitude, double longitude);

    IllegalZone findById(Integer zoneSeq);

    List<IllegalZone> findByCode(List<String> codes, Boolean isSetting);

    List<IllegalZone> findByGeometry(String latitude, String longitude);

    List<IllegalZone> findByIllegalType(String illegalType);

    List<IllegalZone> findByIllegalTypeAndCode(String illegalType, List<String> codes, Boolean isSetting);

    List<IllegalZone> findAll();

    List<IllegalZone> findAllByLimitAndDesc(int limit);

    void save(IllegalZone illegalZone);

    void saveAll(List<IllegalZone> illegalZones);

    void modify(IllegalZone illegalZone);

    void modifyByEvent(Integer zoneSeq, Integer eventSeq);

    void delete(Integer zoneSeq);

}
