package com.snk.door.mongo.entity;

import com.snk.common.annotation.Excel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "visit_record")
public class VisitRecord {

    private static final long serialVersionUID = 1L;
    public static final String ID = "id";
    public static final String DEVICE_NAME = "deviceName";
    public static final String SN = "sn";
    public static final String NAME = "name";
    public static final String FACE = "face";
    public static final String IO_TIME = "ioTime";

    @Id
    private String id;

    /**
     * 卡号
     */
    @Excel(name = "卡号")
    private String cardNo;

    /**
     * 人像
     */
    private String face;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String name;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    private String phone;

    /**
     * 证件类型
     */
    @Excel(name = "证件类型")
    private String idType;

    /**
     * 证件号
     */
    @Excel(name = "证件号")
    private String idNo;

    /**
     * 被访问企业名称
     */
    @Excel(name = "被访问企业名称")
    private String companyName;

    /**
     * 被访问者
     */
    @Excel(name = "被访问者")
    private String leader;

    /**
     * 被访问者手机号
     */
    @Excel(name = "被访问者手机号")
    private String leaderPhone;

    /**
     * 详细地址
     */
    @Excel(name = "详细地址")
    private String address;

    /**
     * 来访事由
     */
    @Excel(name = "来访事由", dictType = "snk_visit_reason")
    private String visitReason;

    /**
     * sn
     */
    private String sn;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 安装位置ID
     */
    @Excel(name = "安装位置ID")
    private String positionId;

    /**
     * 安装位置
     */
    @Excel(name = "安装位置")
    private String positionName;

    /**
     * 出入类型
     */
    @Excel(name = "出入类型", dictType = "snk_io_type")
    private String ioType;

    /**
     * 记录时间
     */
    @Excel(name = "记录时间")
    private String ioTime;

    /**
     * 创建时间
     */
    private String createTime;
}
