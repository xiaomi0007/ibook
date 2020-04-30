package com.xiaomi.ibook.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaomi.ibook.entity.SysUser;
import com.xiaomi.ibook.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by xiaomi
 * on 20/04/28 11:48:00
 */
@Controller
@Slf4j
public class LoginController {

    @Resource
    private SessionRegistry sessionRegistry;

    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/register")
    @ResponseBody
    public String register(String username, String password){
        JSONObject jsonObject = new JSONObject();
        String message = "";
        try {
            if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
                throw new Exception("注册时用户名或密码为空");
            }
            SysUser sysUser = sysUserService.selectByName(username);
            if(sysUser != null){
                throw new Exception("用户名已存在");
            }
            sysUserService.saveNewUser(username,password);
            message = "注册成功,点击登录完成登录";
        } catch (Exception e) {
            log.error("注册时异常:" + e.getMessage());
            message = e.getMessage();
        }
        jsonObject.put("message",message);
        return jsonObject.toJSONString();
    }

    @RequestMapping("/login/error")
    public void loginError(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        AuthenticationException exception = (AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        try {
            response.getWriter().write(exception.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String invalid() {
        return "Session 已过期，请重新登录";
    }

    @GetMapping("/kick")
    @ResponseBody
    public String removeUserSessionByUsername(@RequestParam String username) {
        int count = 0;

        // 获取session中所有的用户信息
        List<Object> users = sessionRegistry.getAllPrincipals();
        for (Object principal : users) {
            if (principal instanceof User) {
                String principalName = ((User)principal).getUsername();
                if (principalName.equals(username)) {
                    // 参数二：是否包含过期的Session
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                            count++;
                        }
                    }
                }
            }
        }
        return "操作成功，清理session共" + count + "个";
    }


    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasPermission('/admin','r')")
    public String printAdminR() {
        return "如果你看见这句话，说明你访问/admin路径具有r权限";
    }

    @RequestMapping("/admin/c")
    @ResponseBody
    @PreAuthorize("hasPermission('/admin','c')")
    public String printAdminC() {
        return "如果你看见这句话，说明你访问/admin路径具有c权限";
    }
}
