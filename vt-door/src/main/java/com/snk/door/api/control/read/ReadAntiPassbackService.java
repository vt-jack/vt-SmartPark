package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.ReadAntiPassback;
import Door.Access.Door8800.Command.Door.Result.ReadAntiPassback_Result;
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
 * 读取防潜回
 */
@Component
public class ReadAntiPassbackService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadAntiPassbackService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadAntiPassbackService() {
        super(DoorOperation.READ_ANTI_PASSBACK);
    }

    /**
     * 读取防潜回
     * @param device
     */
    public void readAntiPassback(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp(device.getId());
        DoorPort_Parameter par = new DoorPort_Parameter(commandDetail, device.getControlPort());
        ReadAntiPassback cmd = new ReadAntiPassback(par);
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
            ReadAntiPassback_Result result = (ReadAntiPassback_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            // 更新防浅回
            Device device = new Device();
            device.setId(doorRsp.getId());
            device.setAntiBack(result.Use);
            deviceService.updateDevice(device);
            log.info("命令完成：{},结果：{}", getOperation().getResult(), result.Use);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
