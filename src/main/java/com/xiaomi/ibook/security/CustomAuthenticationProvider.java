package com.xiaomi.ibook.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by xiaomi
 * on 20/04/29 09:04:00
 */
@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String inputName = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();
        log.info("获取到用户名[{}],开始验证该用户是否能顺利登陆系统.",inputName);
        // userDetails为数据库中查询到的用户信息
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(inputName);
        // 如果是自定义AuthenticationProvider，需要手动密码校验
        if(!userDetails.getPassword().equals(inputPassword)) {
            throw new BadCredentialsException("密码错误");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, inputPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 这里不要忘记，和UsernamePasswordAuthenticationToken比较
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
