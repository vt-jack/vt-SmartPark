package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 开门密码信息对象 snk_user
 * 
 * @author snk
 * @date 2020-08-03
 */
@Data
public class Pwd extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 开门密码 */
    private String password;

    /** 开门次数 */
    private Integer openTimes;

    /** 有效期 */
    private String expiry;

    /** 设备ID */
    private Long deviceId;

    /** 设备名称 */
    private String deviceName;

    /** 安装位置 */
    private String positionName;

    /** 控制板类型 */
    private String modelType;

    /** 设备SN */
    private String sn;

    /** 设备控制端口 */
    private Integer controlPort;

    /** 返回结果 */
    private String result;

    /** 删除标志 */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("password", getPassword())
            .append("openTimes", getOpenTimes())
            .append("expiry", getExpiry())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("positionName", getPositionName())
            .append("modelType", getModelType())
            .append("sn", getSn())
            .append("controlPort", getControlPort())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
