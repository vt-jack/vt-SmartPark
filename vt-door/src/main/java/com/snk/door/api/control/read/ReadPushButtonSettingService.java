package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.ReadPushButtonSetting;
import Door.Access.Door8800.Command.Door.Result.ReadPushButtonSetting_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 读取出门按钮功能
 */
@Component
public class ReadPushButtonSettingService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadRelayReleaseTimeService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadPushButtonSettingService() {
        super(DoorOperation.READ_PUSH_BUTTON_SETTING);
    }

    /**
     * 读取出门按钮功能
     * @param device
     */
    public void readPushButtonSetting(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp(device.getId());
        DoorPort_Parameter par = new DoorPort_Parameter(commandDetail, device.getControlPort());
        par.Door = device.getControlPort();
        ReadPushButtonSetting cmd = new ReadPushButtonSetting(par);
        // 添加命令到队列
        _Allocator.AddCommand(cmd);
    }

    /**
     * 当命令完成时，会触发此函数回调
     *
     * @param cmd 此次事件所关联的命令详情
     * @param incr 命令包含的结果
     */
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult incr) {
        try {
            ReadPushButtonSetting_Result result = (ReadPushButtonSetting_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            // 更新开门按钮设置
            Device device = new Device();
            device.setId(doorRsp.getId());
            device.setUseButton(result.Use);
            device.setNormallyOpen(result.NormallyOpen);
            deviceService.updateDevice(device);
            log.info("命令完成：{},是否启用出门按钮：{},长按出门按钮开关5秒保持常开:{}", getOperation().getResult(), result.Use, result.NormallyOpen);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
