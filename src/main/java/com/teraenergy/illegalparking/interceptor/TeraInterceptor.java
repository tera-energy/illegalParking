package com.teraenergy.illegalparking.interceptor;

import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Slf4j
@Component
public class TeraInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoService userDtoService;


    /**
     * Controller 가 수행되기 전에
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request != null) {}
        return true;
    }

    /**
     * Controller 의 메소드가 수행된 후, View 를 호출하기 전에
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.get(auth.getName());

            if ( user != null) {
                UserDto userDto = userDtoService.get(user);

                // _user
                modelAndView.getModel().put("_user", userDto);
                switch (user.getRole()) {
                    case ADMIN:
                        modelAndView.getModel().put("mainTitle", "불법주정차");
                        break;
                    case GOVERNMENT:
                        modelAndView.getModel().put("mainTitle", "불법주정차 신고접수");
                        break;
                }
            }
        }
    }

    /**
     * View 작업까지 완료된 후 Client에 응답하기 직전에
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }



}
