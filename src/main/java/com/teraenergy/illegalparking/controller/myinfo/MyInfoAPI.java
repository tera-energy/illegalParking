package com.teraenergy.illegalparking.controller.myinfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Date : 2022-10-02
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyInfoAPI {

    private final UserService userService;
    private final UserDtoService userDtoService;

    @PostMapping("/myInfo/get")
    @ResponseBody
    public Object get(@RequestBody String body) throws TeraException {
        JsonNode requestJson = JsonUtil.toJsonNode(body);
        Integer userSeq = requestJson.get("userSeq").asInt();
        User user = userService.get(userSeq);
        user.setDecryptPassword();
        return user;
    }

    @PostMapping("/myInfo/set")
    @ResponseBody
    public Object set(@RequestBody String body) throws TeraException, JsonProcessingException {
        JsonNode requestJson = JsonUtil.toJsonNode(body);
        Integer userSeq = requestJson.get("userSeq").asInt();

        String roleStr = requestJson.get("role").asText();
        String password = requestJson.get("password").asText();
        String name = requestJson.get("name").asText();

        User user = userService.get(userSeq);
        user.setPassword(password);
        user.setEncyptPassword();
        user.setRole(Role.valueOf(roleStr));
        user.setName(name);
        user = userService.set(user);

        UserDto userDto = userDtoService.get(user);
        return userDto;
    }

    @PostMapping("/api/myInfo/get")
    @ResponseBody
    public Object getByApi(Device device, @RequestBody String body) throws TeraException {
        if (device.isMobile()) {
            JsonNode requestJson = JsonUtil.toJsonNode(body);
            Integer userSeq = requestJson.get("userSeq").asInt();
            User user = userService.get(userSeq);
            UserDto userDto = userDtoService.get(user);
            return userDto;
        } else {
            return null;
        }
    }

    @PostMapping("/api/myInfo/set")
    @ResponseBody
    public Object setByApi(Device device, @RequestBody String body) throws TeraException {
        if (device.isMobile()) {
            HashMap<String, Object> resultMap = Maps.newHashMap();
            JsonNode requestJson = JsonUtil.toJsonNode(body);
            Integer userSeq = requestJson.get("userSeq").asInt();

            String roleStr = requestJson.get("role").asText();
            String password = requestJson.get("password").asText();
            String name = requestJson.get("name").asText();

            User user = userService.get(userSeq);
            user.setPassword(password);
            user.setEncyptPassword();
            user.setRole(Role.valueOf(roleStr));
            user.setName(name);
            user = userService.set(user);
            return userDtoService.get(user);
        } else {
            return null;
        }
    }

}
