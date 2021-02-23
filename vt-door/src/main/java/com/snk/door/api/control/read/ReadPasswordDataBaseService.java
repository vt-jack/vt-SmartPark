package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Password.ReadPasswordDataBase;
import Door.Access.Door8800.Command.Password.Result.ReadPasswordDataBase_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 读取开门密码
 */
@Component
public class ReadPasswordDataBaseService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadPasswordDataBaseService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadPasswordDataBaseService() {
        super(DoorOperation.READ_PASSWORD_DATA_BASE);
    }

    /**
     * 读取开门密码
     * @param device 设备信息
     */
    public void readPasswordDataBase(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), device.getModelType());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        if (ModelType.COMMON.getValue().equals(device.getModelType())) {    // 普通版
            ReadPasswordDataBase cmd = new ReadPasswordDataBase(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {    // 高级版
            Door.Access.Door89H.Command.Password.ReadPasswordDataBase cmd = new Door.Access.Door89H.Command.Password.ReadPasswordDataBase(par);
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
            ReadPasswordDataBase_Result result = (ReadPasswordDataBase_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            String modelType = doorRsp.getParam().toString();  // 控制板类型
            List<Device> deviceList = deviceService.selectDeviceBySn(doorRsp.getSn());
            if (ModelType.COMMON.getValue().equals(modelType)) {    // 普通版
                List<PasswordDetail> pwdList = result.PasswordDetails; // 密码列表
                pwdList.stream().forEach(item -> {
                    log.info("开门密码：{}", item.Password);
                    for (int i = 1; i <= deviceList.size(); i++) {
                        log.info("门" + i + "：{}", item.GetDoor(i));
                    }
                });
            } else { // 高级版
                List<Door.Access.Door89H.Command.Data.PasswordDetail> pwdList = result.PasswordDetails; // 密码列表
                pwdList.stream().forEach(item -> {
                    log.info("开门密码：{},开门次数：{},有效期：{}", item.Password, item.OpenTimes, item.Expiry);
                    for (int i = 1; i <= deviceList.size(); i++) {
                        log.info("门" + i + "：{}", item.GetDoor(i));
                    }
                });
            }
            log.info("命令完成：{},结果：{}", getOperation().getResult(), result.DataBaseSize);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
