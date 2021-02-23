package com.snk.door.enums;

/**
 * 设备工作状态
 */
public enum WorkState {
    /**
     * 在线
     */
    ON_LINE("1"),

    /**
     * 离线
     */
    OFF_LINE("2");

    private String value;

    WorkState(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static WorkState getWorkState(String value) {
        for (WorkState workState : WorkState.values()) {
            if (workState.getValue().equals(value)) {
                return workState;
            }
        }
        return null;
    }
}
