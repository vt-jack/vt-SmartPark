package com.snk.door.enums;

/**
 * 卡片状态
 */
public enum CardStatus {
    /**
     * 正常
     */
    NORMAL(Byte.valueOf("0")),

    /**
     * 挂失
     */
    LOSS(Byte.valueOf("1")),

    /**
     * 黑名单
     */
    BLACKLIST(Byte.valueOf("2"));

    private Byte value;

    CardStatus(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return this.value;
    }

    public static CardStatus getCardStatus(Byte value) {
        for (CardStatus cardStatus : CardStatus.values()) {
            if (cardStatus.getValue().equals(value)) {
                return cardStatus;
            }
        }
        return null;
    }
}
