package com.xiaomi.ibook.security;

/**
 * Created by xiaomi
 * on 20/04/29 09:14:00
 */

import com.xiaomi.ibook.entity.SysPermission;
import com.xiaomi.ibook.service.SysPermissionService;
import com.xiaomi.ibook.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Resource
    private SysPermissionService permissionService;
    @Resource
    private SysRoleService roleService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object targetPermission) {
        User user = (User)authentication.getPrincipal();
        log.info("开始为用户[{}]添加权限.",user.getUsername());
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for(GrantedAuthority authority : authorities) {
            String roleName = authority.getAuthority();
            Integer roleId = roleService.selectByName(roleName).getId();
            List<SysPermission> permissionList = permissionService.listByRoleId(roleId);
            for(SysPermission sysPermission : permissionList) {
                List permissions = sysPermission.getPermissions();
                if(targetUrl.equals(sysPermission.getUrl())
                        && permissions.contains(targetPermission)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
