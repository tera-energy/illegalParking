package com.teraenergy.illegalparking.model.dto.user.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.domain.UserGovernmentDto;
import com.teraenergy.illegalparking.model.dto.user.enums.UserGovernmentFilterColumn;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2022-10-11
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class UserGovernmentDtoServiceImpl implements UserGovernmentDtoService{

    private final UserService userService;

    private final UserGroupService userGroupService;

    private final IllegalGroupServcie illegalGroupServcie;

    private final IllegalZoneService illegalZoneService;

    private final ReportService reportService;

    @Override
    public Page<UserGovernmentDto> gets(int pageNumber, int pageSize, UserGovernmentFilterColumn userGovernmentFilterColumn, String search) throws TeraException {

        Page<User> userPages = userService.getsByGovernmentRole(pageNumber, pageSize, userGovernmentFilterColumn, search);

        List<UserGovernmentDto> userGovernmentDtos = Lists.newArrayList();

        for (User user : userPages.getContent()) {
            UserGovernmentDto userGovernmentDto = new UserGovernmentDto();
            userGovernmentDto.setUserSeq(user.getUserSeq());
            userGovernmentDto.setUserName(user.getUsername());
            user.setDecryptPassword();
            userGovernmentDto.setPassword(user.getPassword());
            userGovernmentDto.setLocationType(user.getGovernMentOffice().getLocationType());
            userGovernmentDto.setOfficeName(user.getGovernMentOffice().getName());


            List<UserGroup> userGroups = userGroupService.getsByUser(user.getUserSeq());

            if (userGroups != null) {
                userGovernmentDto.setGroupCount(userGroups.size());
                List<Integer> groupSeqs = userGroups.stream().map(userGroup -> userGroup.getGroupSeq()).collect(Collectors.toList());
                List<IllegalZone> illegalZones = illegalZoneService.gets(groupSeqs);

                // 신고 건수
                userGovernmentDto.setTotalCount(reportService.getSizeForReport(illegalZones));
                // 미처리 건수 (신고 제외)
                userGovernmentDto.setExceptionCount(reportService.getSizeForException(illegalZones));
                // 처리 건수 (과태료대상)
                userGovernmentDto.setPenaltyCount(reportService.getSizeForPenalty(illegalZones));
                // 대기 건수 (신고 접수)
                userGovernmentDto.setCompleteCount(reportService.getSizeForCOMPLETE(illegalZones));
            } else {
                userGovernmentDto.setTotalCount(0);
                userGovernmentDto.setExceptionCount(0);
                userGovernmentDto.setPenaltyCount(0);
                userGovernmentDto.setCompleteCount(0);
            }

            userGovernmentDtos.add(userGovernmentDto);
        }

        int total = (int) userPages.getTotalElements();
        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<UserGovernmentDto> page = new PageImpl<UserGovernmentDto>(userGovernmentDtos, pageRequest, total);
        return page;
    }

    @Override
    public List<IllegalGroup> gets(Integer userSeq) {
        List<UserGroup> userGroups = userGroupService.getsByUser(userSeq);
        List<Integer> groupSeqs = userGroups.stream().map(userGroup -> userGroup.getGroupSeq()).collect(Collectors.toList());
        return illegalGroupServcie.gets(groupSeqs);
    }


}
