package com.xiaomi.ibook.dao;

import com.xiaomi.ibook.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by xiaomi
 * on 20/04/28 11:43:00
 */
@Mapper
public interface SysUserRoleMapper {
    @Select("SELECT * FROM sys_user_role WHERE user_id = #{userId}")
    List<SysUserRole> listByUserId(Integer userId);
}
