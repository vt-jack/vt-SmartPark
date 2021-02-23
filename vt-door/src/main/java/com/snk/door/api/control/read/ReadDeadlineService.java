package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.ReadDeadline;
import Door.Access.Door8800.Command.System.Result.ReadDeadline_Result;
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
 * 读取设备有效期
 */
@Component
public class ReadDeadlineService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadDeadlineService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadDeadlineService() {
        super(DoorOperation.READ_DEAD_LINE);
    }

    /**
     * 读取设备有效期
     * @param device
     */
    public void readDeadline(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadDeadline cmd = new ReadDeadline(par);
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
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            ReadDeadline_Result result = (ReadDeadline_Result) incr;
            // 更新设备有效期
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setDeadLine(result.Deadline);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{},结果：{}", getOperation().getResult(), result.Deadline);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
