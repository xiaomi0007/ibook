package com.xiaomi.ibook.service;

import com.xiaomi.ibook.dao.SysRoleMapper;
import com.xiaomi.ibook.entity.SysRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xiaomi
 * on 20/04/28 11:45:00
 */
@Service
public class SysRoleService {
    @Resource
    private SysRoleMapper roleMapper;

    public SysRole selectById(Integer id){
        return roleMapper.selectById(id);
    }

    public SysRole selectByName(String name) {
        return roleMapper.selectByName(name);
    }
}
