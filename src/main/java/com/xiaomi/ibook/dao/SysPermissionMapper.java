package com.xiaomi.ibook.dao;

/**
 * Created by xiaomi
 * on 20/04/29 09:12:00
 */

import com.xiaomi.ibook.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysPermissionMapper {
    @Select("SELECT * FROM sys_permission WHERE role_id=#{roleId}")
    List<SysPermission> listByRoleId(Integer roleId);
}
