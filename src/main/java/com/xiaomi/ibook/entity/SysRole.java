package com.xiaomi.ibook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by xiaomi
 * on 20/04/28 10:27:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole implements Serializable {

    private Integer id;

    private String name;
}
