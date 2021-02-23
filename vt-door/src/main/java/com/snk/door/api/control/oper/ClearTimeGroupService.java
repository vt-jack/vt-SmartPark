package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.TimeGroup.ClearTimeGroup;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.ITimeSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 清空开门时段
 */
@Component
public class ClearTimeGroupService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ClearTimeGroupService.class);

    @Autowired
    private ITimeSlotService timeSlotService;
    @Autowired
    private IDeviceService deviceService;

    public ClearTimeGroupService() {
        super(DoorOperation.CLEAR_TIME_GROUP);
    }

    /**
     * 清空开门时段
     */
    public void clearTimeGroup(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), device.getParam());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ClearTimeGroup cmd = new ClearTimeGroup(par);
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
            doorRsp.setResult(getOperation().getResult());
            timeSlotService.deleteTimeSlotBySn(doorRsp.getSn());
            if (!ObjectUtils.isEmpty(doorRsp.getParam()) && (Boolean) doorRsp.getParam()) {
                List<Device> deviceList = deviceService.selectDeviceBySn(doorRsp.getSn());
                timeSlotService.insertDefaultTimeSlot(deviceList.get(0));
            } else {
                timeSlotService.deleteTimeSlotByIdx(doorRsp.getSn(), 1);
            }
            // 发送消息
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
