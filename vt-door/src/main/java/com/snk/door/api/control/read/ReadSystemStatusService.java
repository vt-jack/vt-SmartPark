package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.ReadSystemStatus;
import Door.Access.Door8800.Command.System.Result.ReadSystemStatus_Result;
import Face.System.ReadSystemRunStatus;
import Face.System.Result.ReadSystemRunStatus_Result;
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
 * 读取设备运行信息
 */
@Component
public class ReadSystemStatusService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadSystemStatusService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadSystemStatusService() {
        super(DoorOperation.READ_SYSTEM_STATUS);
    }

    /**
     * 读取设备运行信息
     * @param device
     */
    public void readSystemStatus(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        if (isControl(device)) {
            ReadSystemStatus cmd = new ReadSystemStatus(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {
            ReadSystemRunStatus cmd = new ReadSystemRunStatus(par);
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
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            // 更新设备运行信息
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            Integer runDay, formatCount, restartCount;
            if (incr instanceof ReadSystemStatus_Result) {
                ReadSystemStatus_Result result = (ReadSystemStatus_Result) incr;
                runDay = result.RunDay;
                formatCount = result.FormatCount;
                restartCount = result.RestartCount;
                device.setUps(result.UPS);
                device.setTemperature(result.Temperature);
                device.setVoltage(result.Voltage);
                device.setStartTime(DateUtils.getTimeByCalendar(result.StartTime));
            } else {
                ReadSystemRunStatus_Result result = (ReadSystemRunStatus_Result) incr;
                runDay = result.runDay;
                formatCount = result.formatCount;
                restartCount = result.restartCount;
                device.setStartTime(result.startTime);
            }
            device.setRunDay(runDay);
            device.setFormatCount(formatCount);
            device.setRestartCount(restartCount);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
