package com.teraenergy.illegalparking.controller.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.user.service.UserGovernmentDtoService;
import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.GovernmentOffice;
import com.teraenergy.illegalparking.model.entity.governmentoffice.service.GovernmentOfficeService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.service.UserGroupService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-10-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Transactional
@RequiredArgsConstructor
@Controller
public class UserAPI {

    private final UserGovernmentDtoService userGovernmentDtoService;

    private final IllegalGroupServcie illegalGroupServcie;

    private final UserGroupService userGroupService;

    private final UserService userService;

    private final GovernmentOfficeService governmentOfficeService;

    @PostMapping(value = "/user/userGroup/gets")
    @ResponseBody
    public Object getGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);

        List<UserGroup> userGroups = userGroupService.getsByUser(jsonNode.get("userSeq").asInt());

        List<HashMap<String, Object>> userGroupDtos = Lists.newArrayList();
        for (UserGroup userGroup : userGroups) {
            HashMap<String, Object> userGroupDto = Maps.newHashMap();
            userGroupDto.put("userGroupSeq", userGroup.getUserGroupSeq());
            IllegalGroup illegalGroup = illegalGroupServcie.get(userGroup.getGroupSeq());
            userGroupDto.put("name", illegalGroup.getName());
            userGroupDtos.add(userGroupDto);
        }

        return userGroupDtos;
    }

    @PostMapping(value = "/user/userGroup/group/name/get")
    @ResponseBody
    public Object getName(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        LocationType locationType = LocationType.valueOf(jsonNode.get("locationType").asText());
        return illegalGroupServcie.getsNameByUserGroup(locationType);
    }

    @PostMapping("/user/userGroup/set")
    @ResponseBody
    public Object setUserGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer userSeq = jsonNode.get("userSeq").asInt();
        LocationType locationType = LocationType.valueOf(jsonNode.get("locationType").asText());
        String name = jsonNode.get("name").asText();
        IllegalGroup illegalGroup = illegalGroupServcie.get(locationType, name);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserSeq(userSeq);
        userGroup.setGroupSeq(illegalGroup.getGroupSeq());

        if ( userGroupService.isExist(userSeq, illegalGroup.getGroupSeq())) {
            throw new TeraException(TeraExceptionCode.USER_GROUP_IS_EXIST);
        }

        userGroup = userGroupService.set(userGroup);

        HashMap<String, Object> result = Maps.newHashMap();
        result.put("userGroupSeq", userGroup.getUserGroupSeq());
        result.put("name", name);
        return result;
    }

    @PostMapping("/user/userGroup/remove")
    @ResponseBody
    public Object removeUserGroup(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userGroupSeq = jsonNode.get("userGroupSeq").asInt();
            userGroupService.remove(userGroupSeq);
            return "complete";
        } catch (TeraException e){
            throw new TeraException(e.getCode());
        }
    }

    @PostMapping("/user/modify")
    @ResponseBody
    public Object modifyUser(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            User user = userService.get(userSeq);

            String userName = jsonNode.get("userName").asText();
            user.setUsername(userName);

            String password = jsonNode.get("password").asText();
            user.setPassword(password);

            userService.set(user);
            return "changed .. ";
        } catch (Exception e) {
            throw  new TeraException( TeraExceptionCode.USER_FAIL_CHANGE);
        }
    }

    @PostMapping("/user/set")
    @ResponseBody
    public Object setUser(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);

            GovernmentOffice governmentOffice = new GovernmentOffice();
            governmentOffice.setLocationType(LocationType.valueOf(jsonNode.get("locationType").asText().trim()));
            governmentOffice.setName(jsonNode.get("name").asText().trim());

            if ( governmentOfficeService.isExist(governmentOffice.getName(), governmentOffice.getLocationType()) ) {
                throw  new TeraException( TeraExceptionCode.USER_IS_EXIST);
            }
            governmentOffice = governmentOfficeService.set(governmentOffice);

            User user = new User() ;
            String userName = jsonNode.get("userName").asText();
            user.setUsername(userName);
            String password = jsonNode.get("password").asText();
            user.setPassword(password);
            user.setName("공공기관");
            user.setRole(Role.GOVERNMENT);
            user.setUserCode(2L);

            user.setGovernMentOffice(governmentOffice);
            userService.set(user);
            return "registered .. ";
        } catch (Exception e) {
            throw  new TeraException( TeraExceptionCode.USER_FAIL_RESiSTER);
        }
    }

}
