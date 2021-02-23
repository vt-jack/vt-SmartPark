package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.Parameter.WriteDeadline_Parameter;
import Door.Access.Door8800.Command.System.WriteDeadline;
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
 * 写入控制器有效期
 */
@Component
public class WriteDeadlineService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WriteDeadlineService.class);

    @Autowired
    private IDeviceService deviceService;

    public WriteDeadlineService() {
        super(DoorOperation.WRITE_DEAD_LINE);
    }

    /**
     * 写入控制器有效期
     * @param device
     */
    public void writeDeadline(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        Integer deadline = Integer.valueOf(device.getParam().toString());
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), deadline);
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        WriteDeadline_Parameter par = new WriteDeadline_Parameter(commandDetail, deadline);
        WriteDeadline cmd = new WriteDeadline(par);
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
            // 更新记录存储方式
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setDeadLine(Integer.valueOf(doorRsp.getParam().toString()));
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
