package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 考勤设备对象 snk_cwa_device
 *
 * @author snk
 * @date 2020-10-28
 */
@Data
public class CwaDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备ID */
    private Long deviceId;

    /** 设备名称 */
    private String deviceName;

    /** 安装位置 */
    private String positionName;

    /** 删除标志 */
    private String delFlag;

}
