package com.snk.door.enums;

/**
 * 报警记录
 */
public enum AlarmRecord {
    /**
     * 门磁报警
     */
    TYPE_1(1, "门磁报警"),

    /**
     * 匪警报警
     */
    TYPE_2(2, "匪警报警"),

    /**
     * 消防报警
     */
    TYPE_3(3, "消防报警"),

    /**
     * 非法卡刷报警
     */
    TYPE_4(4, "非法卡刷报警"),

    /**
     * 胁迫报警
     */
    TYPE_5(5, "胁迫报警"),

    /**
     * 消防报警(命令通知)
     */
    TYPE_6(6, "消防报警(命令通知)"),

    /**
     * 烟雾报警
     */
    TYPE_7(7, "烟雾报警"),

    /**
     * 防盗报警
     */
    TYPE_8(8, "防盗报警"),

    /**
     * 黑名单报警
     */
    TYPE_9(9, "黑名单报警"),

    /**
     * 开门超时报警
     */
    TYPE_10(10, "开门超时报警"),

    /**
     * 门磁报警撤销
     */
    TYPE_11(0x11, "门磁报警撤销"),

    /**
     * 匪警报警撤销
     */
    TYPE_12(0x12, "匪警报警撤销"),

    /**
     * 消防报警撤销
     */
    TYPE_13(0x13, "消防报警撤销"),

    /**
     * 非法卡刷报警撤销
     */
    TYPE_14(0x14, "非法卡刷报警撤销"),

    /**
     * 胁迫报警撤销
     */
    TYPE_15(0x15, "胁迫报警撤销"),

    /**
     * 撤销烟雾报警
     */
    TYPE_16(0x17, "撤销烟雾报警"),

    /**
     * 关闭防盗报警
     */
    TYPE_17(0x18, "关闭防盗报警"),

    /**
     * 关闭黑名单报警
     */
    TYPE_18(0x19, "关闭黑名单报警"),

    /**
     * 关闭开门超时报警
     */
    TYPE_19(0x1A, "关闭开门超时报警"),

    /**
     * 门磁报警撤销(命令通知)
     */
    TYPE_20(0x21, "门磁报警撤销(命令通知)"),

    /**
     * 匪警报警撤销(命令通知)
     */
    TYPE_21(0x22, "匪警报警撤销(命令通知)"),

    /**
     * 消防报警撤销(命令通知)
     */
    TYPE_22(0x23, "消防报警撤销(命令通知)"),

    /**
     * 非法卡刷报警撤销(命令通知)
     */
    TYPE_23(0x24, "非法卡刷报警撤销(命令通知)"),

    /**
     * 胁迫报警撤销(命令通知)
     */
    TYPE_24(0x25, "胁迫报警撤销(命令通知)"),

    /**
     * 撤销烟雾报警(命令通知)
     */
    TYPE_25(0x27, "撤销烟雾报警(命令通知)"),

    /**
     * 关闭防盗报警(软件关闭)
     */
    TYPE_26(0x28, "关闭防盗报警(软件关闭)"),

    /**
     * 关闭黑名单报警(软件关闭)
     */
    TYPE_27(0x29, "关闭黑名单报警(软件关闭)"),

    /**
     * 关闭开门超时报警
     */
    TYPE_28(0x2A, "关闭开门超时报警");

    private Integer value;

    private String comment;

    AlarmRecord(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static AlarmRecord getAlarmRecord(Short value) {
        for (AlarmRecord alarmRecord : AlarmRecord.values()) {
            if (alarmRecord.getValue().equals(Integer.valueOf(value))) {
                return alarmRecord;
            }
        }
        return null;
    }

    public static String getComment(Short value) {
        for (AlarmRecord alarmRecord : AlarmRecord.values()) {
            if (alarmRecord.getValue().equals(Integer.valueOf(value))) {
                return alarmRecord.getComment();
            }
        }
        return null;
    }
}
