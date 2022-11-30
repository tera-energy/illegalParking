package com.teraenergy.illegalparking.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.util.JsonUtil;
import com.teraenergy.illegalparking.util.enums.JsonUtilModule;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
@Aspect
@Component
public class AopController {

    @Around("execution(* com.teraenergy.illegalparking.controller..*Controller.*(..)) ")
    public Object controllerProcessing(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            return errMsg(e.getMessage());
        }

        return object;
    }

    @Around("execution(* com.teraenergy.illegalparking.controller..*API.*(..)) ")
    public Object apiProcessing(ProceedingJoinPoint joinPoint) {
        HashMap<String, Object> result = Maps.newHashMap();
        try {
            result.put("success", true);
            result.put("msg", "");
            result.put("data", joinPoint.proceed());
        } catch (TeraException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("code", e.getCode());
            result.put("msg", e.getMessage());
            result.put("data", "");
        } catch (Throwable e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("code", TeraExceptionCode.UNKNOWN);
            result.put("msg", e.getMessage());
            result.put("data", "");
        } finally {
            return JsonUtil.toString(result, JsonUtilModule.HIBERNATE);
        }
    }

    @Around("execution(* com.teraenergy.illegalparking.controller.login.LoginController.*(..)) ")
    public Object loginProcessing(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return errMsg(e.getMessage());
        }

        return object;
    }


    /**
     *  @AfterThrows 방법를 이용하는 방법도 있음.
     */
    public Object errMsg(String message){
        // HttpServletRequest 접근 방법
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("msg", message);
        return "/normal/controller/error/500";
    }
}
