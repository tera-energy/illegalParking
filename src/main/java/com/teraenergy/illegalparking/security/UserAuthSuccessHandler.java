package com.teraenergy.illegalparking.security;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class UserAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private  final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        User user = null;
        try {
            user = userService.get((String) authentication.getPrincipal());
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("hashcode", request.getSession().getId().hashCode());


        // default targetUrl 로 이동하지 않도록 설정
        super.setAlwaysUseDefaultTargetUrl(true);

        // default 페이지 설정
        super.setDefaultTargetUrl("/");

        // 페이지 이동
        super.onAuthenticationSuccess(request, response, authentication);

//        // targetUrl 파라메터 이름 설정
//        super.setTargetUrlParameter("redirectUrl");

    }

}

