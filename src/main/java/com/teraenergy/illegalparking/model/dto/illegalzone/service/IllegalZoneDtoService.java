package com.teraenergy.illegalparking.model.dto.illegalzone.service;

import com.teraenergy.illegalparking.model.dto.illegalzone.domain.IllegalZoneDto;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalZoneDtoService {
    IllegalZoneDto getToIllegalZoneDto(IllegalZone illegalZone);

    IllegalZone getToIllegalZone(IllegalZoneDto illegalZoneDto);
}
