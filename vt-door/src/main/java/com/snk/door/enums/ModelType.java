package com.snk.door.enums;

import Door.Access.Connector.E_ControllerType;
import com.snk.common.utils.StringUtils;

/**
 * 控制板类型
 */
public enum ModelType {
    /**
     * 普通板
     */
    COMMON("1", E_ControllerType.Door8800),

    /**
     * 高级板
     */
    HIGH("2", E_ControllerType.Door8900);

    private String value;

    private E_ControllerType controllerType;

    ModelType(String value, E_ControllerType controllerType) {
        this.value = value;
        this.controllerType = controllerType;
    }

    public String getValue() {
        return this.value;
    }

    public E_ControllerType getControllerType() {
        return controllerType;
    }

    public static ModelType getModelType(String value) {
        for (ModelType modelType : ModelType.values()) {
            if (modelType.getValue().equals(value)) {
                return modelType;
            }
        }
        return null;
    }

    public static E_ControllerType getControllerType(String value) {
        if (StringUtils.isEmpty(value)) {
            return HIGH.getControllerType();
        }
        for (ModelType modelType : ModelType.values()) {
            if (modelType.getValue().equals(value)) {
                return modelType.getControllerType();
            }
        }
        return null;
    }
}
