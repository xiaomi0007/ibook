package com.xiaomi.ibook.service;

import com.xiaomi.ibook.dao.SysUserMapper;
import com.xiaomi.ibook.entity.SysUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xiaomi
 * on 20/04/28 11:44:00
 */
@Service
public class SysUserService {

    @Resource
    private SysUserMapper userMapper;

    public SysUser selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public SysUser selectByName(String name) {
        return userMapper.selectByName(name);
    }

    public void saveNewUser(String username,String password) throws Exception{
        SysUser sysUser = new SysUser(3,username,encryptPassword(password));
        int id = userMapper.insert(sysUser);
        if(id == 0){
            throw new Exception("插入不成功");
        }
    }

    private String encryptPassword(String password) {
        // BCryptPasswordEncoder 加密
        return new BCryptPasswordEncoder().encode(password);
    }

}
