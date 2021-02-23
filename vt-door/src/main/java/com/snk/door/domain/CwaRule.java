package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 考勤规则对象 snk_cwa_rule
 * 
 * @author snk
 * @date 2020-10-28
 */
@Data
public class CwaRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 考勤规则名称 */
    private String name;

    /** 考勤规则详情 */
    private String ruleJson;

    /** 超过多少分钟算迟到 */
    private Integer lateMm;

    /** 迟到/早退多少分钟算旷工半天 */
    private Integer absHalf;

    /** 迟到/早退多少分钟算旷工一天 */
    private Integer absDay;

    /** 下班多少分钟以后记为加班 */
    private Integer otMm;

    /** 删除标志 */
    private String delFlag;

}
