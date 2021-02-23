package com.snk.door.enums;

/**
 * 记录类型
 */
public enum RecordType {
    /**
     * 刷卡记录
     */
    CARD(1, "刷卡记录"),

    /**
     * 出门按钮记录
     */
    BUTTON(2, "出门按钮记录"),

    /**
     * 门磁记录
     */
    DOOR(3, "门磁记录"),

    /**
     * 软件操作记录
     */
    SOFTWARE(4, "软件操作记录"),

    /**
     * 报警记录
     */
    ALARM(5, "报警记录"),

    /**
     * 系统记录
     */
    SYSTEM(6, "系统记录");

    private Integer value;

    private String comment;

    RecordType(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static RecordType getRecordType(Integer value) {
        for (RecordType recordType : RecordType.values()) {
            if (recordType.getValue().equals(value)) {
                return recordType;
            }
        }
        return null;
    }
}
