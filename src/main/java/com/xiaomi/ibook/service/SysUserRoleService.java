package com.xiaomi.ibook.service;

import com.xiaomi.ibook.dao.SysUserRoleMapper;
import com.xiaomi.ibook.entity.SysUserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xiaomi
 * on 20/04/28 11:48:00
 */
@Service
public class SysUserRoleService {
    @Resource
    private SysUserRoleMapper userRoleMapper;

    public List<SysUserRole> listByUserId(Integer userId) {
        return userRoleMapper.listByUserId(userId);
    }
}
