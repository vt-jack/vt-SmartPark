package com.snk.door.domain;

import lombok.Data;

@Data
public class AutoUser {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long userId;

    /** 姓名 */
    private String name;

    /** 部门名称 */
    private String deptName;

}
