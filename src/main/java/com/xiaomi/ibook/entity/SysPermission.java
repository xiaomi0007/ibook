package com.xiaomi.ibook.entity;

/**
 * Created by xiaomi
 * on 20/04/29 09:10:00
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 权限实体类
 * @author jitwxs
 * @since 2018/5/15 18:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer id;

    private String url;

    private Integer roleId;

    private String permission;

    private List<String> permissions;

    // 省略除permissions外的getter/setter

    public List<String> getPermissions() {
        return Arrays.asList(this.permission.trim().split(","));
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
