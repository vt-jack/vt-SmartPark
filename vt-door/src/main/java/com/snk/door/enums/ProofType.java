package com.snk.door.enums;

/**
 * 凭证类型
 */
public enum ProofType {
    /**
     * 人脸
     */
    FACE("1"),

    /**
     * 指纹
     */
    FINGER("2");

    private String value;

    ProofType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static ProofType getProofType(String value) {
        for (ProofType proofType : ProofType.values()) {
            if (proofType.getValue().equals(value)) {
                return proofType;
            }
        }
        return null;
    }
}
