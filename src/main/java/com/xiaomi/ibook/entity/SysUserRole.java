package com.xiaomi.ibook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by xiaomi
 * on 20/04/28 11:40:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer roleId;
}
