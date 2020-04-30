package com.xiaomi.ibook.service;

/**
 * Created by xiaomi
 * on 20/04/29 09:12:00
 */

import com.xiaomi.ibook.dao.SysPermissionMapper;
import com.xiaomi.ibook.entity.SysPermission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class SysPermissionService {
    @Resource
    private SysPermissionMapper permissionMapper;

    /**
     * 获取指定角色所有权限
     */
    public List<SysPermission> listByRoleId(Integer roleId) {
        return permissionMapper.listByRoleId(roleId);
    }
}
