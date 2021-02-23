package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 开门时段对象 snk_device_time_slot
 * 
 * @author snk
 * @date 2020-08-03
 */
@Data
public class TimeSlot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备SN */
    private String sn;

    /** 1-64 开门时间段 */
    private Integer idx;

    /** 时段名称 */
    private String name;

    /** 时段  {“time1”: [{"start": “00:00”, "end": "01:59"}, {"start": “02:00”, "end": "08:00"}]} */
    private String timeGroup;

    /** 删除标志 */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sn", getSn())
            .append("idx", getIdx())
            .append("name", getName())
            .append("timeGroup", getTimeGroup())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
