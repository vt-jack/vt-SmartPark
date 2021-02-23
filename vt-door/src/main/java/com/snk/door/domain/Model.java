package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备型号信息对象 snk_user
 * 
 * @author snk
 * @date 2020-08-03
 */
@Data
public class Model extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备型号 */
    private String model;

    /** 设备类型 */
    private String deviceType;

    /** 控制门数量 */
    private String port;

    /** 控制板类型 */
    private String type;

    /** 描述 */
    private String comment;

    /** 设备图片 */
    private String image;

    /** 接线图 */
    private String conScheme;

    /** 删除标志 */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("model", getModel())
            .append("type", getType())
            .append("comment", getComment())
            .append("image", getImage())
            .append("conScheme", getConScheme())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
