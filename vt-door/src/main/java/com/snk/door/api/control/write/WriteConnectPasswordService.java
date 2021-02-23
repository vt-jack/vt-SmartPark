package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.Parameter.WriteConnectPassword_Parameter;
import Door.Access.Door8800.Command.System.WriteConnectPassword;
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
 * 修改控制器密码
 */
@Component
public class WriteConnectPasswordService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WriteConnectPasswordService.class);

    @Autowired
    private IDeviceService deviceService;

    public WriteConnectPasswordService() {
        super(DoorOperation.WRITE_CONNECT_PASSWORD);
    }

    /**
     * 修改密码
     * @param device
     */
    public void writeConnectPassword(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        String newPwd = device.getParam().toString();
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), newPwd);
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        WriteConnectPassword_Parameter par = new WriteConnectPassword_Parameter(commandDetail, newPwd);
        WriteConnectPassword cmd = new WriteConnectPassword(par);
        // 添加命令到队列
        _Allocator.AddCommand(cmd);
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
            // 更新设备通讯密码
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setPwd(doorRsp.getParam().toString());
            device.setUpdateBy(doorRsp.getUserName());
            device.setUpdateTime(DateUtils.getNowDate());
            deviceService.updateDeviceBySn(device);
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}
