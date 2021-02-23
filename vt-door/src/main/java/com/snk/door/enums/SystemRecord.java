package com.snk.door.enums;

/**
 * 系统记录
 */
public enum SystemRecord {
    /**
     * 系统加电
     */
    TYPE_1(1, "系统加电"),

    /**
     * 系统错误复位（看门狗）
     */
    TYPE_2(2, "系统错误复位（看门狗）"),

    /**
     * 设备格式化记录
     */
    TYPE_3(3, "设备格式化记录"),

    /**
     * 系统高温记录，温度大于>75
     */
    TYPE_4(4, "系统高温记录，温度大于>75"),

    /**
     * 系统UPS供电记录
     */
    TYPE_5(5, "系统UPS供电记录"),

    /**
     * 温度传感器损坏，温度大于>100
     */
    TYPE_6(6, "温度传感器损坏，温度大于>100"),

    /**
     * 电压过低，小于<09V
     */
    TYPE_7(7, "电压过低，小于<09V"),

    /**
     * 电压过高，大于>14V
     */
    TYPE_8(8, "电压过高，大于>14V"),

    /**
     * 读卡器接反
     */
    TYPE_9(9, "读卡器接反"),

    /**
     * 读卡器线路未接好
     */
    TYPE_10(10, "读卡器线路未接好"),

    /**
     * 无法识别的读卡器
     */
    TYPE_11(11, "无法识别的读卡器"),

    /**
     * 电压恢复正常，小于14V，大于9V
     */
    TYPE_12(12, "电压恢复正常，小于14V，大于9V"),

    /**
     * 网线已断开
     */
    TYPE_13(13, "网线已断开"),

    /**
     * 网线已插入
     */
    TYPE_14(14, "网线已插入");

    private Integer value;

    private String comment;

    SystemRecord(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static SystemRecord getSystemRecord(Short value) {
        for (SystemRecord systemRecord : SystemRecord.values()) {
            if (systemRecord.getValue().equals(Integer.valueOf(value))) {
                return systemRecord;
            }
        }
        return null;
    }

    public static String getComment(Short value) {
        for (SystemRecord systemRecord : SystemRecord.values()) {
            if (systemRecord.getValue().equals(Integer.valueOf(value))) {
                return systemRecord.getComment();
            }
        }
        return null;
    }
}
