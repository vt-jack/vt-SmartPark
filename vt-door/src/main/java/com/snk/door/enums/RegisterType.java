package com.snk.door.enums;

/**
 * 登记类型
 */
public enum RegisterType {
    /**
     * 请假
     */
    QJ("1"),

    /**
     * 调休
     */
    TX("2"),

    /**
     * 加班
     */
    JB("3"),

    /**
     * 补卡
     */
    BK("4");

    private String value;

    RegisterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static RegisterType getRegisterType(String value) {
        for (RegisterType registerType : RegisterType.values()) {
            if (registerType.getValue().equals(value)) {
                return registerType;
            }
        }
        return null;
    }
}
