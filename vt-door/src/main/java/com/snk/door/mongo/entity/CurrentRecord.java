package com.snk.door.mongo.entity;

import com.snk.common.annotation.Excel;
import com.snk.common.utils.DateUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "current_record")
public class CurrentRecord {

    private static final long serialVersionUID = 1L;
    public static final String ID = "id";
    public static final String DEVICE_ID = "deviceId";
    public static final String DEVICE_NAME = "deviceName";
    public static final String SN = "sn";
    public static final String DEPT_ID = "deptId";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String TYPE = "type";
    public static final String FACE = "face";
    public static final String IO_TIME = "ioTime";
    public static final String CREATE_TIME = "createTime";

    @Id
    private String id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String userName;

    /**
     * 设备SN
     */
    @Excel(name = "SN")
    private String sn;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备集合
     */
    @Transient
    private List<Long> deviceIds;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 设备位置ID
     */
    private Long positionId;

    /**
     * 设备位置
     */
    @Excel(name = "安装位置")
    private String positionName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 证件号
     */
    private String idNo;

    /**
     * 所属部门
     */
    private Long deptId;

    /**
     * 所属部门名称
     */
    @Excel(name = "部门")
    private String deptName;

    /**
     * 岗位
     */
    private Long postId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 凭证类型
     */
    private String proofType;

    /**
     * 序列号
     */
    private Integer serialNumber;

    /**
     * 凭证
     */
    @Excel(name = "凭证", sort = 1)
    private String cardNo;

    /**
     * 人脸
     */
    private String face;

    /**
     * 指纹
     */
    private String finger;

    /**
     * 记录类型 1 刷卡记录|2 按钮记录|3 门磁记录|4 远程控制记录|5 报警记录|6 系统记录
     */
    @Excel(name = "记录类型", dictType = "snk_record_type")
    private Integer type;

    /**
     * 记录编码
     */
    private Short transactionCode;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String describe;

    /**
     * 温度
     */
    @Excel(name = "温度")
    private String temperature;

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
     * 记录日期
     */
    @Transient
    private String ioDate;

    public String getIoDate () {
        if (ObjectUtils.isEmpty(ioTime)) {
            return null;
        }
        return ioTime.split(" ")[0];
    }

    /**
     * 记录时间-开始
     */
    @Transient
    private String beginTime;

    /**
     * 记录时间-结束
     */
    @Transient
    private String endTime;

    /**
     * 创建时间
     */
    private String createTime;
}
