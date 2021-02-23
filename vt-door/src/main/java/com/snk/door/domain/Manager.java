package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备权限表 snk_device_manager
 * 
 * @author snk
 */
@Data
public class Manager extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备位置ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 设备ID */
    private Long deviceId;

    /** 用户姓名 */
    private String userName;

    /** 部门名称 */
    private String deptName;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("deviceId", getDeviceId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
