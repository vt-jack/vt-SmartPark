package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备位置表 snk_device_position
 * 
 * @author snk
 */
@Data
public class Position extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备位置ID */
    private Long id;

    /** 父位置ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 位置名称 */
    private String name;

    /** 显示顺序 */
    private String orderNum;

    /** 描述 */
    private String comment;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /** 父部门名称 */
    private String parentName;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("name", getName())
            .append("orderNum", getOrderNum())
            .append("comment", getComment())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
