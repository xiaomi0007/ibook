package com.xiaomi.ibook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by xiaomi
 * on 20/04/28 10:24:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser implements Serializable {

    private Integer id;

    private String username;

    private String password;

}
