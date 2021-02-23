package com.snk.door.enums;

/**
 * 卡片类型
 */
public enum CardType {
    /**
     * 排序区域
     */
    SORT(1),

    /**
     * 非排序区域
     */
    NOT_SORT(2),

    /**
     * 所有区域
     */
    ALL(3);

    private Integer value;

    CardType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static CardType getCardType(Integer value) {
        for (CardType cardType : CardType.values()) {
            if (cardType.getValue().equals(value)) {
                return cardType;
            }
        }
        return null;
    }
}
