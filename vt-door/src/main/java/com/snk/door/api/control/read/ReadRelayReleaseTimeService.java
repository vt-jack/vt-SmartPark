package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.ReadRelayReleaseTime;
import Door.Access.Door8800.Command.Door.Result.ReadRelayReleaseTime_Result;
import Face.Door.ReadUnlockingTime;
import Face.Door.Result.UnlockingTime_Result;
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
 * 读取开门保持时长
 */
@Component
public class ReadRelayReleaseTimeService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadRelayReleaseTimeService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadRelayReleaseTimeService() {
        super(DoorOperation.READ_RELAY_RELEASE_TIME);
    }

    /**
     * 读取开门保持时长
     * @param device
     */
    public void readRelayReleaseTime(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp(device.getId());
        DoorPort_Parameter par = new DoorPort_Parameter(commandDetail, device.getControlPort());
        par.Door = device.getControlPort();
        if (isControl(device)) {
            ReadRelayReleaseTime cmd = new ReadRelayReleaseTime(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {
            ReadUnlockingTime cmd = new ReadUnlockingTime(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        }
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
            Integer keepTime;
            if (incr instanceof ReadRelayReleaseTime_Result) {
                ReadRelayReleaseTime_Result result = (ReadRelayReleaseTime_Result) incr;
                keepTime = result.ReleaseTime;
            } else {
                UnlockingTime_Result result = (UnlockingTime_Result) incr;
                keepTime = result.ReleaseTime;
            }
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            // 更新开门保持时长
            Device device = new Device();
            device.setId(doorRsp.getId());
            device.setKeepTime(keepTime);
            deviceService.updateDevice(device);
            log.info("命令完成：{},结果：{}", getOperation().getResult(), keepTime);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
