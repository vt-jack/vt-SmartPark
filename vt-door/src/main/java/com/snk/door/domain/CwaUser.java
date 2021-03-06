package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 考勤设备对象 snk_cwa_user
 * 
 * @author snk
 * @date 2020-10-28
 */
@Data
public class CwaUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 人员ID */
    private Long userId;

    /** 考勤规则ID */
    private Long ruleId;

    /** 考勤规则名称 */
    private String ruleName;

    /** 考勤规则详情 */
    private String ruleJson;

    /** 超过多少分钟算迟到/早退 */
    private Integer lateMm;

    /** 迟到/早退多少分钟算旷工半天 */
    private Integer absHalf;

    /** 迟到/早退多少分钟算旷工一天 */
    private Integer absDay;

    /** 下班多少分钟以后记为加班 */
    private Integer otMm;

    /** 人员编号 */
    private String userNo;

    /** 人员姓名 */
    private String userName;

    /** 性别 */
    private String sex;

    /** 所属部门ID */
    private Long deptId;

    /** 所属部门 */
    private String deptName;

    /** 删除标志 */
    private String delFlag;

}
