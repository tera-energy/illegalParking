package com.teraenergy.illegalparking.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Component
public class UserAuthLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // default targetUrl 로 이동하지 않도록 설정
        super.setAlwaysUseDefaultTargetUrl(true);

        // targetUrl 파라메터 이름 설정
        super.setTargetUrlParameter("redirectUrl");

        // 페이지 URL 설정
        super.setDefaultTargetUrl("/login");

        // 페이지 이동
        super.onLogoutSuccess(request, response, authentication);

    }

}
