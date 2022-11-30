package com.teraenergy.illegalparking.model.dto.illegalzone.service;

import com.teraenergy.illegalparking.model.dto.illegalzone.domain.IllegalZoneDto;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class IllegalZoneDtoServiceImpl implements IllegalZoneDtoService {

    private final IllegalGroupServcie illegalGroupServcie;
    private final IllegalEventService illegalEventService;


    @Override
    public IllegalZoneDto getToIllegalZoneDto(IllegalZone illegalZone) {
        IllegalZoneDto illegalZoneDto = new IllegalZoneDto();
        illegalZoneDto.setZoneSeq(illegalZone.getZoneSeq());
        illegalZoneDto.setPolygon(illegalZone.getPolygon());
        illegalZoneDto.setCode(illegalZone.getCode());

        if (illegalZone.getEventSeq() != null) {
            IllegalEvent illegalEvent = illegalEventService.get(illegalZone.getEventSeq());
            IllegalGroup illegalGroup = illegalGroupServcie.get(illegalEvent.getGroupSeq());
            illegalZoneDto.setEventSeq(illegalEvent.getEventSeq());
            illegalZoneDto.setGroupSeq(illegalEvent.getGroupSeq());
            illegalZoneDto.setLocationType( illegalGroup.getLocationType());
            illegalZoneDto.setIllegalType(illegalEvent.getIllegalType().toString());
            illegalZoneDto.setUsedFirst(illegalEvent.isUsedFirst());
            illegalZoneDto.setFirstStartTime(illegalEvent.getFirstStartTime());
            illegalZoneDto.setFirstEndTime(illegalEvent.getFirstEndTime());
            illegalZoneDto.setUsedSecond(illegalEvent.isUsedSecond());
            illegalZoneDto.setSecondStartTime(illegalEvent.getSecondStartTime());
            illegalZoneDto.setSecondEndTime(illegalEvent.getSecondEndTime());
        }
        return illegalZoneDto;
    }

    @Override
    public IllegalZone getToIllegalZone(IllegalZoneDto illegalZoneDto) {

        IllegalZone illegalZone = new IllegalZone();
        if ( illegalZoneDto.getZoneSeq() != null) {
            illegalZone.setZoneSeq(illegalZoneDto.getZoneSeq());
        }
        illegalZone.setPolygon(illegalZoneDto.getPolygon());
        illegalZone.setCode(illegalZone.getCode());
        illegalZone.setIsDel(false);

        if (illegalZoneDto.getEventSeq() != null) {
            IllegalEvent illegalEvent = new IllegalEvent();

            illegalEvent.setEventSeq(illegalZoneDto.getEventSeq());
            illegalEvent.setIllegalType(IllegalType.valueOf(illegalZoneDto.getIllegalType()));
            illegalEvent.setUsedFirst(illegalZoneDto.getUsedFirst());
            illegalEvent.setFirstStartTime(illegalZoneDto.getFirstStartTime());
            illegalEvent.setFirstEndTime(illegalZoneDto.getFirstEndTime());
            illegalEvent.setUsedSecond(illegalZoneDto.getUsedSecond());
            illegalEvent.setSecondStartTime(illegalZoneDto.getSecondStartTime());
            illegalEvent.setSecondEndTime(illegalZoneDto.getSecondEndTime());
            illegalZone.setIllegalEvent(illegalEvent);
        } else {
            illegalZone.setIllegalEvent(null);
        }

        return illegalZone;
    }

    @Override
    public List<IllegalZone> getsToIllegalZones(List<IllegalZoneDto> illegalZoneDtos) {
        List<IllegalZone> illegalZones = Lists.newArrayList();
        for(IllegalZoneDto illegalZoneDto : illegalZoneDtos) {
            illegalZones.add(getToIllegalZone(illegalZoneDto));
        }
        return illegalZones;
    }

}
