package com.teraenergy.illegalparking.security;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserAuthenticationManager implements AuthenticationManager {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String account = authentication.getPrincipal().toString();          // ID
        String password = authentication.getCredentials().toString();       // PASSWORD

        User user = (User) userDetailsService.loadUserByUsername(account);

        if( user == null || !user.isEnabled()) {
            log.error("사용자의 계정을 찾을수 없습니다.");
            throw new UsernameNotFoundException("사용자의 계정을 찾을수 없습니다.");
        }

        try {
            if ( !userService.isUserByUserNameAndPassword(account, password)) {
                log.error("사용자의 계정과 패스워드가 맞지 않습니다.");
                throw new BadCredentialsException("사용자의 계정과 패스워드가 맞지 않습니다.");
            }
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }

        // 권한 정보
        Collection< ? extends GrantedAuthority> authorities = user.getAuthorities();
//        Iterator< ? extends GrantedAuthority> iterator = authorities.iterator();
//        while (iterator.hasNext()) {
//            GrantedAuthority grantedAuthority = iterator.next();
//            log.info(" **** authorities = {}", grantedAuthority.getAuthority());
//        }

        return new UsernamePasswordAuthenticationToken(account, password, authorities);
    }

}
