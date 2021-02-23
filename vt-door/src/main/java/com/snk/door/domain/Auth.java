package com.snk.door.domain;

import com.snk.common.annotation.Excel;
import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 开门权限对象 snk_device_auth
 * 
 * @author snk
 * @date 2020-08-06
 */
@Data
public class Auth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户号")
    private Long userId;

    /** 用户姓名 */
    @Excel(name = "姓名")
    private String userName;

    /** 用户号 */
    @Excel(name = "人员编号")
    private String userNo;

    /** 部门 */
    @Excel(name = "部门")
    private String deptName;

    /** 部门 */
    @Excel(name = "岗位")
    private String postName;

    /** 卡号 */
    @Excel(name = "卡号")
    private String cardNo;

    /** 照片 */
    private String photo;

    /** 指纹 */
    private String finger;

    /** 设备ID */
    private Long deviceId;

    /** 设备名称 */
    private String deviceName;

    /** 设备位置ID */
    private Long positionId;

    /** 设备SN */
    @Excel(name = "SN")
    private String sn;

    /** 安装位置 */
    @Excel(name = "安装位置")
    private String positionName;

    /** 控制端口 */
    private Integer controlPort;

    /** 特殊权限 */
    @Excel(name = "特殊权限", dictType = "snk_special_type")
    private String special;

    /** 开门时段ID */
    private Long slotId;

    /** 开门时段下标 */
    private Integer slotIdx;

    /** 开门时段名称 */
    @Excel(name = "开门时段")
    private String slotName;

    /** 有效期限 */
    @Excel(name = "有效期限")
    private String expTime;

    /** 开门次数 */
    @Excel(name = "开门次数", prompt = "65535代表无限制")
    private Integer openNum;

    /** 禁止节假日开门 */
    @Excel(name = "节假日", dictType = "snk_limit_flag")
    private String holiday;

    /** 是否已上传 */
    @Excel(name = "状态", dictType = "snk_upload_status")
    private String status;

    /** 管理员 */
    @Excel(name = "管理员", dictType = "snk_yes_no")
    private String admin;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /**
     * 卡片状态<br/>
     * 0：正常状态；1：挂失；2：黑名单
     */
    private byte cardStatus;

    /** 删除标志 */
    @Excel(name = "删除标志", dictType = "snk_del_flag")
    private String delFlag;

}
