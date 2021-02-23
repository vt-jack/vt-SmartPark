package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 访问权限对象 snk_visit_auth
 * 
 * @author snk
 * @date 2020-11-18
 */
@Data
public class VisitAuth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 权限名称 */
    private String name;

    /** 开门时段ID */
    private Long slotId;

    /** 开门时段名称 */
    private String slotName;

    /** 开门次数 */
    private Integer openNum;

    /** 有效期限（分钟） */
    private Integer expTime;

    /** 设备信息 */
    private String deviceJson;

    /** 删除标志 */
    private String delFlag;

}
