package com.snk.door.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 手动登记对象 snk_cwa_register
 * 
 * @author snk
 * @date 2020-10-28
 */
@Data
public class CwaRegister extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 登记类型 */
    private String type;

    /** 请假类型 */
    private String leaveType;

    /** 人员ID */
    private Long userId;

    /** 人员姓名 */
    private String userName;

    /** 部门ID */
    private Long deptId;

    /** 部门名称 */
    private String deptName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    private String yyyyMM;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 小时数 */
    private Float leaveTime;

    /** 删除标志 */
    private String delFlag;

}
