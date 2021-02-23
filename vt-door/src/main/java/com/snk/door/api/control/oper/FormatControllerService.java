package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.FormatController;
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
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 初始化设备
 */
@Component
public class FormatControllerService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(FormatControllerService.class);

    @Autowired
    private IDeviceService deviceService;

    public FormatControllerService() {
        super(DoorOperation.FORMAT_CONTROLLER);
    }

    /**
     * 初始化设备
     * @param device
     */
    public void formatController(Device device) {
        //定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp(device.getId());
        CommandParameter par = new CommandParameter(commandDetail);
        FormatController cmd = new FormatController(par);
        //添加命令到队列
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
            doorRsp.setResult(getOperation().getResult());
            Device device = deviceService.selectDeviceById(doorRsp.getId());
            if (!ObjectUtils.isEmpty(device)) {
                List<Device> deviceList = deviceService.selectDeviceBySn(device.getSn());
                deviceList.stream().forEach(item -> deviceService.initDevice(item));
            }
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}
