package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Door.Parameter.WriteRelayReleaseTime_Parameter;
import Door.Access.Door8800.Command.Door.WriteRelayReleaseTime;
import Face.Door.Parameter.UnlockingTime_Parameter;
import Face.Door.WriteUnlockingTime;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
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
 * 写入开锁保持时长
 */
@Component
public class WriteRelayReleaseTimeService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WriteRelayReleaseTimeService.class);

    @Autowired
    private IDeviceService deviceService;

    public WriteRelayReleaseTimeService() {
        super(DoorOperation.WRITE_RELAY_RELEASE_TIME);
    }

    /**
     * 写入开锁保持时长
     * @param device
     */
    public void writeRelayReleaseTime(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        Integer releaseTime = Integer.valueOf(device.getParam().toString());
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp(device.getId(), releaseTime);
        if (isControl(device)) {
            WriteRelayReleaseTime_Parameter par = new WriteRelayReleaseTime_Parameter(commandDetail, device.getControlPort(), releaseTime);
            WriteRelayReleaseTime cmd = new WriteRelayReleaseTime(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {
            UnlockingTime_Parameter par = new UnlockingTime_Parameter(commandDetail, releaseTime);
            WriteUnlockingTime cmd = new WriteUnlockingTime(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        }
    }

    /**
     * 当命令完成时，会触发此函数回调
     *
     * @param cmd 此次事件所关联的命令详情
     * @param result 命令包含的结果
     */
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        try {
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            doorRsp.setResult(getOperation().getResult());
            // 更新保持开门时长
            Device device = new Device();
            device.setId(doorRsp.getId());
            device.setKeepTime(Integer.valueOf(doorRsp.getParam().toString()));
            device.setUpdateBy(doorRsp.getUserName());
            device.setUpdateTime(DateUtils.getNowDate());
            deviceService.updateDevice(device);
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}
