package com.xiaomi.ibook.dao;

import com.xiaomi.ibook.entity.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * Created by xiaomi
 * on 20/04/28 11:42:00
 */
@Mapper
public interface SysUserMapper {
    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser selectById(Integer id);

    @Select("SELECT * FROM sys_user WHERE username = #{name}")
    SysUser selectByName(String name);

    @Insert("insert into sys_user(username,password) values(#{username},#{password})")
    @Options(useGeneratedKeys=true,keyProperty="id",keyColumn="id")
    int insert(SysUser sysUser);

}
