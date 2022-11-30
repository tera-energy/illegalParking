package com.teraenergy.illegalparking.model.dto.illegalzone.service;

import com.teraenergy.illegalparking.model.dto.illegalzone.domain.PointDto;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;

import java.util.List;

/**
 * Date : 2022-10-10
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
public interface PointDtoService {
    List<PointDto> gets(Integer groupSeq);
    PointDto get(Point point);
    PointDto getByPointSeq(Integer pointSeq);
}
