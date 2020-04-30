package com.xiaomi.ibook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xiaomi
 * on 20/04/28 10:49:00
 */
@Controller
@Slf4j
public class PageController {

    @RequestMapping("/")
    public String showHome() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("当前登陆用户：" + name);

        return "main";
    }

    @RequestMapping("/login")
    public String showLogin() {
        return "login";
    }

    @RequestMapping("/home")
    public String batch() {
        return "home";
    }
}
