package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.ReadConnectPassword;
import Door.Access.Door8800.Command.System.Result.ReadConnectPassword_Result;
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
 * 读取控制器密码
 */
@Component
public class ReadConnectPasswordService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadConnectPasswordService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadConnectPasswordService() {
        super(DoorOperation.READ_CONNECT_PASSWORD);
    }

    /**
     * 读取密码
     * @param device
     */
    public void readConnectPassword(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadConnectPassword cmd = new ReadConnectPassword(par);
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
            ReadConnectPassword_Result result = (ReadConnectPassword_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            // 更新设备通讯密码
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setPwd(result.Password);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{},结果：{}", getOperation().getResult(), result.Password);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
