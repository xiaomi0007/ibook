package com.xiaomi.ibook.security;

import com.xiaomi.ibook.entity.SysRole;
import com.xiaomi.ibook.entity.SysUser;
import com.xiaomi.ibook.entity.SysUserRole;
import com.xiaomi.ibook.service.SysRoleService;
import com.xiaomi.ibook.service.SysUserRoleService;
import com.xiaomi.ibook.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xiaomi
 * on 20/04/28 11:52:00
 */
@Service("userDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Resource
    private SysUserService userService;

    @Resource
    private SysRoleService roleService;

    @Resource
    private SysUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始判断用户[{}]是否存在,是否过期,是否锁定等",username);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        SysUser user = userService.selectByName(username);
        // 判断用户是否存在
        if(user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        log.info("开始为用户[{}]添加角色",username);
        // 添加权限
        List<SysUserRole> userRoles = userRoleService.listByUserId(user.getId());
        for (SysUserRole userRole : userRoles) {
            SysRole role = roleService.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        // 返回UserDetails实现类
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
