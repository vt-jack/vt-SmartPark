package com.snk.door.enums;

/**
 * 出门按钮记录
 */
public enum ButtonRecord {
    /**
     * 合法开门
     */
    TYPE_1(1, "合法开门"),

    /**
     * 开门时段过期
     */
    TYPE_2(2, "开门时段过期"),

    /**
     * 锁定时按钮
     */
    TYPE_3(3, "锁定时按钮"),

    /**
     * 控制器已过期
     */
    TYPE_4(4, "控制器已过期"),

    /**
     * 互锁时按钮(不开门)
     */
    TYPE_5(5, "互锁时按钮(不开门)");

    private Integer value;

    private String comment;

    ButtonRecord(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static ButtonRecord getButtonRecord(Short value) {
        for (ButtonRecord buttonRecord : ButtonRecord.values()) {
            if (buttonRecord.getValue().equals(Integer.valueOf(value))) {
                return buttonRecord;
            }
        }
        return null;
    }

    public static String getComment(Short value) {
        for (ButtonRecord buttonRecord : ButtonRecord.values()) {
            if (buttonRecord.getValue().equals(Integer.valueOf(value))) {
                return buttonRecord.getComment();
            }
        }
        return null;
    }
}
