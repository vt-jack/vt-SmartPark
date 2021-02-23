package com.snk.door.enums;

import com.snk.door.socketio.entity.Message;

public enum DoorOperation {

    /** READ */
    /** 读取版本号 */
    READ_VERSION("读取版本号", "读取版本号成功", "读取版本号失败", null, null),

    /** 读取SN */
    READ_SN("读取SN", "读取SN成功", "读取SN失败", null, null),

    /** 读取设备运行信息 */
    READ_SYSTEM_STATUS("读取设备运行信息", "读取设备运行信息成功", "读取设备运行信息失败", null, null),

    /** 读取控制器TCP网络参数 */
    READ_TCP_SETTING("读取控制器TCP网络参数", "读取控制器TCP网络参数成功", "读取控制器TCP网络参数失败", null, null),

    /** 读取设备有效期 */
    READ_DEAD_LINE("读取设备有效期", "读取设备有效期成功", "读取设备有效期失败", null, null),

    /** 读取记录存储方式 */
    READ_RECORD_MODE("读取记录存储方式", "读取记录存储方式成功", "读取记录存储方式失败", null, null),

    /** 读取开锁保持时长 */
    READ_RELAY_RELEASE_TIME("读取开锁保持时长", "读取开锁保持时长成功", "读取开锁保持时长失败", null, null),

    /** 读取出门按钮功能 */
    READ_PUSH_BUTTON_SETTING("读取出门按钮功能", "读取出门按钮功能成功", "读取出门按钮功能失败", null, null),

    /** 采集卡信息 */
    READ_CARD_DATABASE_DETAIL("读取卡信息", "读取卡信息成功", "读取卡信息失败", null, null),

    /** 采集控制器信息 */
    READ_TRANSACTION_DATABASE_DETAIL("读取控制器信息", "读取控制器信息成功", "读取控制器信息失败", null, null),

    /** 读取控制器密码 */
    READ_CONNECT_PASSWORD("读取控制器密码", "读取控制器密码成功", "读取控制器密码失败", null, null),

    /** 读取防潜回 */
    READ_ANTI_PASSBACK("读取防潜回", "读取防潜回成功", "读取防潜回失败", null, null),

    /** 读取开门时段 */
    READ_TIME_GROUP("读取开门时段", "读取开门时段成功", "读取开门时段失败", null, null),

    /** 读取开门密码详情 */
    READ_PASSWORD_DATA_BASE_DETAIL("读取开门密码详情", "读取开门密码详情成功", "读取开门密码详情失败", null, null),

    /** 读取开门密码 */
    READ_PASSWORD_DATA_BASE("读取开门密码", "读取开门密码成功", "读取开门密码失败", null, null),

    /** 读取人员存储详情 */
    READ_PRESON_DATA_BASE_DETAIL("读取人员存储详情", "读取人员存储详情成功", "读取人员存储详情失败", null, null),

    /** 读取所有用户 */
    READ_PRESON_DATA_BASE("读取所有用户", "读取所有用户成功", "读取所有用户失败", null, null),

    /** 读取开门密码 */
    READ_FILE("读取文件", "读取文件成功", "读取文件失败", null, null),

    /** 读取OEM */
    READ_OEM("读取OEM", "读取OEM成功", "读取OEM失败", null, null),

    /** 读取监控开启状态 */
    READ_WATCH_STATE("读取监控开启状态", "读取监控开启状态成功", "读取监控开启状态失败", null, null),
    /** READ */

    /** OPER */
    /** 测试连接 */
    TEST_CONNECT("测试连接", "连接成功", "连接失败", null, Message.ADD_DEVICE),

    /** 初始化设备 */
    FORMAT_CONTROLLER("初始化设备", "初始化设备成功", "初始化设备失败", "door/device/init", Message.OP_DEVICE),

    /** 远程开门 */
    OPEN_DOOR("远程开门", "远程开门成功", "远程开门失败", "door/device/open", Message.OP_DEVICE),

    /** 远程关门 */
    CLOSE_DOOR("远程关门", "远程关门成功", "远程关门失败", "door/device/close", Message.OP_DEVICE),

    /** 远程保持门常开 */
    HOLD_DOOR("设置门常开", "设置门常开成功", "设置门常开失败", "door/device/hold", Message.OP_DEVICE),

    /** 远程锁定门 */
    LOCK_DOOR("锁定门", "锁定门成功", "锁定门失败", "door/device/lock", Message.OP_DEVICE),

    /** 远程解除门锁定 */
    UNLOCK_DOOR("解除门锁定", "解除门锁定成功", "解除门锁定失败", "door/device/unlock", Message.OP_DEVICE),

    /** 重置控制器密码 */
    RESET_CONNECT_PASSWORD("重置控制器密码", "重置控制器密码成功", "重置控制器密码失败", "door/device/resetCp", Message.OP_DEVICE),

    /** 开启数据监控 */
    BEGIN_WATCH("开启数据监控", "开启数据监控成功", "开启数据监控失败", "door/device/watch", Message.WATCH_DEVICE),

    /** 关闭数据监控 */
    CLOSE_WATCH("关闭数据监控", "关闭数据监控成功", "关闭数据监控失败", null, Message.WATCH_DEVICE),

    /** 清空开门时段 */
    CLEAR_TIME_GROUP("清空开门时段", "清空开门时段成功", "清空开门时段失败", "door/device/clearTg", Message.OP_DEVICE),

    /** 清空卡片数据库 */
    CLEAR_CARD_DATABASE("清空权限数据", "清空权限数据成功", "清空权限数据失败", "door/device/clearCdb", Message.CUSTOM),

    /** 禁止通过门上传 */
    BAN_CARD_LIST("禁止通过门", "禁止通过门上传成功", "禁止通过门上传失败", "door/device/banCdb", Message.CUSTOM),

    /** 禁止通过门上传-多设备 */
    BAN_CARD_LIST_BATCH("禁止通过门", "禁止通过门上传成功", "禁止通过门上传失败", "door/device/auth/banCdb", Message.CUSTOM),

    /** 删除卡片 */
    DELETE_CARD("删除卡片", "删除卡片成功", "删除卡片失败", "door/device/deleteCdb", null),

    /** 删除开门密码 */
    DELETE_PASSWORD("删除开门密码", "删除开门密码成功", "删除开门密码失败", "door/device/pwd/deletePwd", Message.CUSTOM),

    /** 清空开门密码 */
    CLEAR_PASSWORD_DATE_BASE("清空开门密码", "清空开门密码成功", "清空开门密码失败", "door/device/pwd/clearPwd", Message.CUSTOM),

    /** 读取新纪录 */
    READ_TRANSACTION_DATABASE("采集纪录", "采集纪录成功", "采集纪录失败", "door/device/record/collect", Message.CUSTOM),

    /** 关闭所有报警 */
    CLOSE_ALARM("关闭所有报警", "关闭所有报警成功", "关闭所有报警失败", "door/device/closeAlarm", Message.OP_DEVICE),
    /** OPER */

    /** WRITE */
    /** 设置设备有效期 */
    WRITE_DEAD_LINE("设置设备有效期", "设置设备有效期成功", "设置设备有效期失败", "door/device/writeDl", Message.OP_DEVICE),

    /** 设置记录存储方式 */
    WRITE_RECORD_MODE("设置记录存储方式", "设置记录存储方式成功", "设置记录存储方式失败", "door/device/writeRm", Message.OP_DEVICE),

    /** 设置开锁保持时长 */
    WRITE_RELAY_RELEASE_TIME("设置开锁保持时长", "写入开锁保持时长成功", "写入开锁保持时长失败", "door/device/writeRrt", Message.OP_DEVICE),

    /** 修改控制器TCP网络参数 */
    WRITE_TCP_SETTING("设置TCP网络参数", "设置控制器TCP网络参数成功", "设置控制器TCP网络参数失败", "door/device/writeTcp", Message.OP_DEVICE),

    /** 设置出门按钮功能 */
    WRITE_PUSH_BUTTON_SETTING("设置出门按钮功能", "设置出门按钮功能成功", "设置出门按钮功能失败", "door/device/writePbs", Message.OP_DEVICE),

    /** 修改控制器密码 */
    WRITE_CONNECT_PASSWORD("设置密码", "设置密码成功", "设置密码失败", "door/device/writeCp", Message.OP_DEVICE),

    /** 设置开门时段 */
    WRITE_TIME_GROUP("设置开门时段", "设置开门时段成功", "设置开门时段失败", "door/device/writeTg", Message.OP_DEVICE),

    /** 允许通过门上传 */
    WRITE_CARD_LIST("允许通过门", "允许通过门上传成功", "允许通过门上传失败", "door/device/writeCdb", Message.CUSTOM),

    /** 允许通过门上传-多设备 */
    WRITE_CARD_LIST_BATCH("允许通过门", "允许通过门上传成功", "允许通过门上传失败", "door/device/auth/writeCdb", Message.CUSTOM),

    /** 添加开门密码 */
    WRITE_PASSWORD("开门密码", "添加开门密码成功", "添加开门密码失败", "door/device/pwd/writePwd", Message.CUSTOM),

    /** 写入防潜回 */
    WRITE_ANTI_PASSBACK("设置防潜回", "设置防潜回成功", "设置防潜回失败", "door/device/writeAp", Message.OP_DEVICE);
    /** WRITE */

    private String describe;

    private String result;

    private String errorMsg;

    private String url;

    private String event;


    DoorOperation(String describe, String result, String errorMsg, String url, String event) {
        this.describe = describe;
        this.result = result;
        this.errorMsg = errorMsg;
        this.url = url;
        this.event = event;
    }

    public String getDescribe() {
        return this.describe;
    }

    public String getResult() {
        return this.result;
    }

    public String getUrl() {
        return this.url;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public String getEvent() { return this.event; }
}
