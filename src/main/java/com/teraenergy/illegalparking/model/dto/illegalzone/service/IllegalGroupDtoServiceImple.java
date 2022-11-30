package com.teraenergy.illegalparking.model.dto.illegalzone.service;

import com.teraenergy.illegalparking.model.dto.illegalzone.domain.IllegalGroupDto;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.enums.GroupFilterColumn;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-09
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class IllegalGroupDtoServiceImple implements IllegalGroupDtoService {

    private final IllegalGroupServcie illegalGroupServcie;

    private final IllegalEventService illegalEventService;

    private final PointService pointService;

    @Override
    public List<IllegalGroupDto> gets(List<IllegalGroup> illegalGroups) {

        List<IllegalGroupDto> illegalGroupDtos = Lists.newArrayList();
        for (IllegalGroup illegalGroup : illegalGroups) {
            IllegalGroupDto illegalGroupDto = new IllegalGroupDto();
            illegalGroupDto.setGroupSeq(illegalGroup.getGroupSeq());
            illegalGroupDto.setName(illegalGroup.getName());
            illegalGroupDto.setLocationType(illegalGroup.getLocationType());
            long groupSize = illegalEventService.getSizeInGroup(illegalGroup.getGroupSeq());
            illegalGroupDto.setGroupSize(groupSize);
            Point point = pointService.getInGroup(illegalGroup.getGroupSeq());
            String note = "";
            if ( point != null) {
                if(point.getIsPointLimit()) {
                    note = "포인트 제한없음 / ";
                } else  {
                    note = point.getLimitValue() == null ? "이벤트 0 point / " : "이벤트 " + point.getLimitValue() + " point / ";
                }

                if ( point.getIsTimeLimit()) {
                    note += "날짜 제한 없음";
                } else {
                    note += StringUtil.convertDateToString(point.getStartDate(), "yyyy-MM-dd");
                    note += " ~ ";
                    note += StringUtil.convertDateToString(point.getStopDate(), "yyyy-MM-dd");
                    note += " ...";
                }
            }
            illegalGroupDto.setNote(note);
            illegalGroupDtos.add(illegalGroupDto);
        }
        return illegalGroupDtos;
    }

    @Override
    public Page<IllegalGroupDto> gets(Integer pageNumber, Integer pageSize, GroupFilterColumn filterColumn, String search) {
        Page<IllegalGroup> illegalGroupPage = illegalGroupServcie.get(pageNumber, pageSize, filterColumn, search);
        List<IllegalGroupDto> illegalGroupDtos = gets(illegalGroupPage.getContent());

        pageNumber = pageNumber - 1;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<IllegalGroupDto> page = new PageImpl<IllegalGroupDto>(illegalGroupDtos, pageRequest, illegalGroupPage.getTotalElements());
        return page;
    }
}
