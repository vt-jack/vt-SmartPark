package com.snk.door.enums;

/**
 * 刷卡记录-人脸|指纹
 */
public enum CardRecordFace {
    /**
     * 刷卡验证
     */
    TYPE_1(1, "刷卡验证"),

    /**
     * 指纹验证
     */
    TYPE_2(2, "指纹验证"),

    /**
     * 人脸验证
     */
    TYPE_3(3, "人脸验证"),

    /**
     * 指纹 + 刷卡
     */
    TYPE_4(4, "指纹+刷卡"),

    /**
     * 人脸 + 指纹
     */
    TYPE_5(5, "人脸+指纹"),

    /**
     * 人脸 + 刷卡
     */
    TYPE_6(6, "人脸+刷卡"),

    /**
     * 刷卡 + 密码
     */
    TYPE_7(7, "刷卡+密码"),

    /**
     * 人脸 + 密码
     */
    TYPE_8(8, "人脸+密码"),

    /**
     * 指纹 + 密码
     */
    TYPE_9(9, "指纹+密码"),

    /**
     * 手动输入用户号加密码验证
     */
    TYPE_10(10, "手动输入用户号加密码验证"),

    /**
     * 指纹+刷卡+密码
     */
    TYPE_11(11, "指纹+刷卡+密码"),

    /**
     * 人脸+刷卡+密码
     */
    TYPE_12(12, "人脸+刷卡+密码"),

    /**
     * 人脸+指纹+密码
     */
    TYPE_13(13, "人脸+指纹+密码"),

    /**
     * 人脸+指纹+刷卡
     */
    TYPE_14(14, "人脸+指纹+刷卡"),

    /**
     * 重复验证
     */
    TYPE_15(15, "重复验证"),

    /**
     * 有效期过期
     */
    TYPE_16(16, "有效期过期"),

    /**
     * 开门时段过期
     */
    TYPE_17(17, "开门时段过期"),

    /**
     * 节假日时不能开门
     */
    TYPE_18(18, "节假日时不能开门"),

    /**
     * 未注册用户
     */
    TYPE_19(19, "未注册用户"),

    /**
     * 探测锁定
     */
    TYPE_20(20, "探测锁定"),

    /**
     * 有效次数已用尽
     */
    TYPE_21(21, "有效次数已用尽"),

    /**
     * 锁定时验证，禁止开门
     */
    TYPE_22(22, "锁定时验证，禁止开门"),

    /**
     * 挂失卡
     */
    TYPE_23(23, "挂失卡"),

    /**
     * 黑名单卡
     */
    TYPE_24(24, "黑名单卡"),

    /**
     * 免验证开门
     */
    TYPE_25(25, "免验证开门"),

    /**
     * 禁止刷卡验证
     */
    TYPE_26(26, "禁止刷卡验证"),

    /**
     * 禁止指纹验证
     */
    TYPE_27(27, "禁止指纹验证"),

    /**
     * 控制器已过期
     */
    TYPE_28(28, "控制器已过期"),

    /**
     * 验证通过—有效期即将过期
     */
    TYPE_29(29, "验证通过—有效期即将过期");

    private Integer value;

    private String comment;

    CardRecordFace(Integer value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getComment() {
        return comment;
    }

    public static CardRecordFace getCardRecordFace(Short value) {
        for (CardRecordFace cardRecordFace : CardRecordFace.values()) {
            if (cardRecordFace.getValue().equals(Integer.valueOf(value))) {
                return cardRecordFace;
            }
        }
        return null;
    }

    public static String getComment(Short value) {
        for (CardRecordFace cardRecordFace : CardRecordFace.values()) {
            if (cardRecordFace.getValue().equals(Integer.valueOf(value))) {
                return cardRecordFace.getComment();
            }
        }
        return null;
    }

    public static Boolean isCard(Short value) {
        Integer val = Integer.valueOf(value);
        if (CardRecordFace.TYPE_1.getValue().equals(val) ||
                CardRecordFace.TYPE_4.getValue().equals(val) ||
                CardRecordFace.TYPE_6.getValue().equals(val) ||
                CardRecordFace.TYPE_7.getValue().equals(val) ||
                CardRecordFace.TYPE_11.getValue().equals(val) ||
                CardRecordFace.TYPE_12.getValue().equals(val) ||
                CardRecordFace.TYPE_14.getValue().equals(val)) {
            return true;
        }
        return false;
    }

    public static Boolean isFace(Short value) {
        Integer val = Integer.valueOf(value);
        if (CardRecordFace.TYPE_3.getValue().equals(val) ||
                CardRecordFace.TYPE_5.getValue().equals(val) ||
                CardRecordFace.TYPE_6.getValue().equals(val) ||
                CardRecordFace.TYPE_8.getValue().equals(val) ||
                CardRecordFace.TYPE_12.getValue().equals(val) ||
                CardRecordFace.TYPE_13.getValue().equals(val) ||
                CardRecordFace.TYPE_14.getValue().equals(val)) {
            return true;
        }
        return false;
    }

    public static Boolean isFinger(Short value) {
        Integer val = Integer.valueOf(value);
        if (CardRecordFace.TYPE_2.getValue().equals(val) ||
                CardRecordFace.TYPE_4.getValue().equals(val) ||
                CardRecordFace.TYPE_5.getValue().equals(val) ||
                CardRecordFace.TYPE_9.getValue().equals(val) ||
                CardRecordFace.TYPE_11.getValue().equals(val) ||
                CardRecordFace.TYPE_13.getValue().equals(val) ||
                CardRecordFace.TYPE_14.getValue().equals(val)) {
            return true;
        }
        return false;
    }

}
