package com.snk.door.enums;

/**
 * 设备类型
 */
public enum DeviceType {
    /**
     * 控制板
     */
    CONTROL("1"),

    /**
     * 人脸
     */
    FACE("2"),

    /**
     * 指纹
     */
    FINGER("3");

    private String value;

    DeviceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static DeviceType getDeviceType(String value) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (deviceType.getValue().equals(value)) {
                return deviceType;
            }
        }
        return null;
    }
}
