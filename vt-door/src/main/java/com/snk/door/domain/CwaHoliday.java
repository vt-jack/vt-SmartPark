package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 节假日对象 snk_cwa_holiday
 * 
 * @author snk
 * @date 2020-10-31
 */
@Data
public class CwaHoliday extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 年度 */
    private Integer year;

    /** 节假日 */
    private String type;

    /** 放假时间 */
    private String holiday;

    /** 调班时间 */
    private String tbDate;

    /** 禁止开门 0 否  1 是 */
    private String banDoor;

    /** 备注 */
    private String remark;

    /** 删除标志 */
    private String delFlag;

}
