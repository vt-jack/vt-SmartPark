package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 设备信息对象 snk_device
 * 
 * @author snk
 * @date 2020-08-03
 */
@Data
public class Device extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 多ID */
    private String[] ids;

    /** 位置ID */
    private Long positionId;

    /** 位置名称 */
    private String positionName;

    /** 设备名称 */
    private String name;

    /** 是否自动获取IP */
    private Boolean autoIp;

    /** mac地址 */
    private String mac;

    /** IP */
    private String ip;

    /** 子网掩码 */
    private String ipMask;

    /** 网关地址 */
    private String ipGateway;

    /** TCP端口 */
    private Integer tcpPort;

    /** UDP端口 */
    private Integer udpPort;

    /** 服务器IP */
    private String serverIp;

    /** 服务器地址 */
    private String serverAddress;

    /** 服务器端口 */
    private Integer serverPort;

    /** DNS */
    private String dns;

    /** DNS备份 */
    private String dnsBak;

    /** 控制端口 */
    private Integer controlPort;

    /** SN */
    private String sn;

    /** new SN */
    private String newSn;

    /** 密码 */
    private String pwd;

    /** 型号 */
    private String model;

    /** 设备类型 */
    private String deviceType;

    /** 控制板类型 */
    private String modelType;

    /** 开锁保持时长 */
    private Integer keepTime;

    /** 存储方式 */
    private Short recordMode;

    /** 卡片数据库类型 */
    private Integer cardType;

    /** 版本 */
    private String ver;

    /** 人脸版本 */
    private String verFace;

    /** 指纹 */
    private String verFinger;

    /** 算法版本 */
    private String algoritVer;

    /** 设备有效期 */
    private Integer deadLine;

    /** 是否启用出门按钮 */
    private Boolean useButton;

    /** 长按出门按钮开关5秒保持常开 */
    private Boolean normallyOpen;

    /** 系统运行天数 */
    private Integer runDay;

    /** 格式化次数 */
    private Integer formatCount;

    /** 看门狗复位次数 */
    private Integer restartCount;

    /** 系统温度 */
    private Float temperature;

    /** 上电时间 */
    private String startTime;

    /** 电压 */
    private Float voltage;

    /** UPS供电状态 */
    private Integer ups;

    /** 防潜回 */
    private Boolean antiBack;

    /** 出厂日期 */
    private String delivery;

    /** 制造商名称 */
    private String manufacturer;

    /** 用户容量 */
    private Integer userCapacity;

    /** 已使用户容量 */
    private Integer userUsed;

    /** 卡容量 */
    private Integer cardCapacity;

    /** 已使用卡容量 */
    private Integer cardUsed;

    /** 人脸容量 */
    private Integer faceCapacity;

    /** 已使用人脸容量 */
    private Integer faceUsed;

    /** 指纹容量 */
    private Integer fpCapacity;

    /** 已使用指纹容量 */
    private Integer fpUsed;

    /** 打卡记录容量 */
    private Integer clockCapacity;

    /** 已使用打卡记录容量 */
    private Integer clockUsed;

    /** 出门按钮记录容量 */
    private Integer buttonCapacity;

    /** 已使用出门按钮记录容量 */
    private Integer buttonUsed;

    /** 门磁记录容量 */
    private Integer doorCapacity;

    /** 已使用门磁记录容量 */
    private Integer doorUsed;

    /** 远程记录容量 */
    private Integer softwareCapacity;

    /** 已使用远程记录容量 */
    private Integer softwareUsed;

    /** 打卡报警容量 */
    private Integer alarmCapacity;

    /** 已使用报警记录容量 */
    private Integer alarmUsed;

    /** 系统记录容量 */
    private Integer sysCapacity;

    /** 已使用系统记录容量 */
    private Integer sysUsed;

    /** 体温记录容量 */
    private Integer bodyCapacity;

    /** 已使用体温记录容量 */
    private Integer bodyUsed;

    /** 工作状态 */
    private String workState;

    /** 设备图片 */
    private String image;

    /** 接线图 */
    private String conScheme;

    /** 型号描述 */
    private String comment;

    /** 删除标志 */
    private String delFlag;

    /** 参数 */
    private Object param;

    /** 分组字段 */
    private String groupBy;

}
