package com.snk.door.enums;

/**
 * 系统记录-人脸|指纹
 */
public enum SystemRecordFace {
    /**
     * 软件开门
     */
    TYPE_1(1, "软件开门"),

    /**
     * 软件关门
     */
    TYPE_2(2, "软件关门"),

    /**
     * 软件常开
     */
    TYPE_3(3, "软件常开"),

    /**
     * 控制器自动进入常开
     */
    TYPE_4(4, "控制器自动进入常开"),

    /**
     * 控制器自动关闭门
     */
    TYPE_5(5, "控制器自动关闭门"),

    /**
     * 长按出门按钮常开
     */
    TYPE_6(6, "长按出门按钮常开"),

    /**
     * 长按出门按钮常闭
     */
    TYPE_7(7, "长按出门按钮常闭"),

    /**
     * 软件锁定
     */
    TYPE_8(8, "软件锁定"),

    /**
     * 软件解除锁定
     */
    TYPE_9(9, "软件解除锁定"),

    /**
     * 控制器定时锁定--到时间自动锁定
     */
    TYPE_10(10, "控制器定时锁定-到时间自动锁定"),

    /**
     * 控制器定时锁定--到时间自动解除锁定
     */
    TYPE_11(11, "控制器定时锁定-到时间自动解除锁定"),

    /**
     * 报警--锁定
     */
    TYPE_12(12, "报警-锁定"),

    /**
     * 报警--解除锁定
     */
    TYPE_13(13, "报警-解除锁定"),

    /**
     * 非法认证报警
     */
    TYPE_14(14, "非法认证报警"),

    /**
     * 门磁报警
     */
    TYPE_15(15, "门磁报警"),

    /**
     * 胁迫报警
     */
    TYPE_16(16, "胁迫报警"),

    /**
     * 开门超时报警
     */
    TYPE_17(17, "开门超时报警"),

    /**
     * 黑名单报警
     */
    TYPE_18(18, "黑名单报警"),

    /**
     * 消防报警
     */
    TYPE_19(19, "消防报警"),

    /**
     * 防拆报警
     */
    TYPE_20(20, "防拆报警"),

    /**
     * 非法认证报警解除
     */
    TYPE_21(21, "非法认证报警解除"),

    /**
     * 门磁报警解除
     */
    TYPE_22(22, "门磁报警解除"),

    /**
     * 胁迫报警解除
     */
    TYPE_23(23, "胁迫报警解除"),

    /**
     * 开门超时报警解除
     */
    TYPE_24(24, "开门超时报警解除"),

    /**
     * 黑名单报警解除
     */
    TYPE_25(25, "黑名单报警解除"),

    /**
     * 消防报警解除
     */
    TYPE_26(26, "消防报警解除"),

    /**
     * 防拆报警解除
     */
    TYPE_27(27, "防拆报警解除"),

    /**
     * 系统加电
     */
    TYPE_28(28, "系统加电"),

    /**
     * 非法认证报警
     */
    TYPE_29(29, "系统错误复位（看门狗）"),

    /**
     * 设备格式化记录
     */
    TYPE_30(30, "设备格式化记录"),

    /**
     * 读卡器接反
     */
    TYPE_31(31, "读卡器接反"),

    /**
     * 读卡器线路未接好
     */
    TYPE_32(32, "读卡器线路未接好"),

    /**
     * 无法识别的读卡器
     */
    TYPE_33(33, "无法识别的读卡器"),

    /**
     * 网线已断开
     */
    TYPE_34(34, "网线已断开"),

    /**
     * 网线已插入
     */
    TYPE_35(35, "网线已插入"),

    /**
     * WIFI 已连接
     */
    TYPE_36(36, "WIFI 已连接"),

    /**
     * WIFI 已断开
     */
    TYPE_37(37, "WIFI 已断开");

    private Integer value;

    private String comment;

    SystemRecordFace(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static SystemRecordFace getSystemRecordFace(Short value) {
        for (SystemRecordFace systemRecordFace : SystemRecordFace.values()) {
            if (systemRecordFace.getValue().equals(Integer.valueOf(value))) {
                return systemRecordFace;
            }
        }
        return null;
    }

    public static String getComment(Short value) {
        for (SystemRecordFace systemRecordFace : SystemRecordFace.values()) {
            if (systemRecordFace.getValue().equals(Integer.valueOf(value))) {
                return systemRecordFace.getComment();
            }
        }
        return null;
    }
}
