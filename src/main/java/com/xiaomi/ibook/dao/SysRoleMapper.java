package com.xiaomi.ibook.dao;

import com.xiaomi.ibook.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by xiaomi
 * on 20/04/28 11:42:00
 */
@Mapper
public interface SysRoleMapper {
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(Integer id);


    @Select("SELECT * FROM sys_role WHERE name = #{name}")
    SysRole selectByName(String name);
}
