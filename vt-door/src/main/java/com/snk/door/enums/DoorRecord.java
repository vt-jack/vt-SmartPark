package com.snk.door.enums;

/**
 * 门磁记录
 */
public enum DoorRecord {
    /**
     * 开门
     */
    TYPE_1(1, "开门"),

    /**
     * 关门
     */
    TYPE_2(2, "关门"),

    /**
     * 进入门磁报警状态
     */
    TYPE_3(3, "进入门磁报警状态"),

    /**
     * 退出门磁报警状态
     */
    TYPE_4(4, "退出门磁报警状态"),

    /**
     * 门未关好
     */
    TYPE_5(5, "门未关好"),

    /**
     * 使用按钮开门
     */
    TYPE_6(6, "使用按钮开门"),

    /**
     * 按钮开门时门已锁定
     */
    TYPE_7(7, "按钮开门时门已锁定"),

    /**
     * 按钮开门时控制器已过期
     */
    TYPE_8(8, "按钮开门时控制器已过期");

    private Integer value;

    private String comment;

    DoorRecord(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static DoorRecord getDoorRecord(Short value) {
        for (DoorRecord doorRecord : DoorRecord.values()) {
            if (doorRecord.getValue().equals(Integer.valueOf(value))) {
                return doorRecord;
            }
        }
        return null;
    }

    public static String getComment(Short value) {
        for (DoorRecord doorRecord : DoorRecord.values()) {
            if (doorRecord.getValue().equals(Integer.valueOf(value))) {
                return doorRecord.getComment();
            }
        }
        return null;
    }
}
