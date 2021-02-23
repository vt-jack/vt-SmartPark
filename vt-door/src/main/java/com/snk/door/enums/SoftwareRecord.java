package com.snk.door.enums;

/**
 * 软件操作记录
 */
public enum SoftwareRecord {
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
     * 互锁时远程开门
     */
    TYPE_14(14, "互锁时远程开门");

    private Integer value;

    private String comment;

    SoftwareRecord(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static SoftwareRecord getSoftwareRecord(Short value) {
        for (SoftwareRecord softwareRecord : SoftwareRecord.values()) {
            if (softwareRecord.getValue().equals(Integer.valueOf(value))) {
                return softwareRecord;
            }
        }
        return null;
    }

    public static String getComment(Short value) {
        for (SoftwareRecord softwareRecord : SoftwareRecord.values()) {
            if (softwareRecord.getValue().equals(Integer.valueOf(value))) {
                return softwareRecord.getComment();
            }
        }
        return null;
    }
}
