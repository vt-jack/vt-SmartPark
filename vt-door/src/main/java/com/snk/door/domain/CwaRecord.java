package com.snk.door.domain;

import com.snk.common.annotation.Excel;
import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 考勤报表对象 snk_cwa_record
 * 
 * @author snk
 * @date 2020-10-31
 */
@Data
public class CwaRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 人员ID */
    private Long userId;

    /** 人员姓名 */
    @Excel(name = "姓名")
    private String userName;

    /** 年度 */
    @Excel(name = "年份")
    private Integer year;

    /** 月份 */
    @Excel(name = "月份")
    private Integer month;

    /** 应出勤天数 */
    @Excel(name = "应出勤天数")
    private Integer beWork;

    /** 实际出勤天数 */
    @Excel(name = "实际出勤天数")
    private Integer doWork;

    /** 迟到次数 */
    @Excel(name = "迟到次数")
    private Integer lateCount;

    /** 迟到(分钟) */
    @Excel(name = "迟到(分钟)")
    private Integer lateM;

    /** 早退次数 */
    @Excel(name = "早退次数")
    private Integer outCount;

    /** 早退(分钟) */
    @Excel(name = "早退(分钟)")
    private Integer outM;

    /** 缺勤次数 */
    @Excel(name = "缺勤次数")
    private Integer absenceCount;

    /** 缺勤(分钟) */
    @Excel(name = "缺勤(分钟)")
    private Integer absenceM;

    /** 请假次数 */
    @Excel(name = "请假次数")
    private Integer leaveCount;

    /** 请假(小时) */
    @Excel(name = "请假(小时)")
    private Float leaveH;

    /** 调休次数 */
    @Excel(name = "调休次数")
    private Integer fellowCount;

    /** 调休(小时) */
    @Excel(name = "调休(小时)")
    private Float fellowH;

    /** 加班次数 */
    @Excel(name = "加班次数")
    private Integer otCount;

    /** 加班(小时) */
    @Excel(name = "加班(小时)")
    private Float otH;

    /** 旷工次数 */
    @Excel(name = "旷工次数")
    private Integer absCount;

    /** 旷工天数 */
    @Excel(name = "旷工天数")
    private Float absD;

    /** 补卡次数 */
    @Excel(name = "补卡次数")
    private Integer reCard;

    /** 考勤详情 */
    @Excel(name = "考勤详情")
    private String itemJson;

    /** 删除标志 */
    private String delFlag;

}
