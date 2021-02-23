package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TransactionDetail;
import Door.Access.Door8800.Command.Transaction.ReadTransactionDatabaseDetail;
import Door.Access.Door8800.Command.Transaction.Result.ReadTransactionDatabaseDetail_Result;
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
 * 读取控制器信息
 */
@Component
public class ReadTransactionDatabaseDetailService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadTransactionDatabaseDetailService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadTransactionDatabaseDetailService() {
        super(DoorOperation.READ_TRANSACTION_DATABASE_DETAIL);
    }

    /**
     * 读取控制器信息
     * @param device
     */
    public void readTransactionDatabaseDetail(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        if (isControl(device)) {
            ReadTransactionDatabaseDetail cmd = new ReadTransactionDatabaseDetail(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {
            Face.Transaction.ReadTransactionDatabaseDetail cmd = new Face.Transaction.ReadTransactionDatabaseDetail(par);
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
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            TransactionDetail card, button, door, software, alarm, system;
            if (incr instanceof ReadTransactionDatabaseDetail_Result) {
                ReadTransactionDatabaseDetail_Result result = (ReadTransactionDatabaseDetail_Result) incr;
                card = result.DatabaseDetail.CardTransactionDetail;   // 卡相关记录
                button = result.DatabaseDetail.ButtonTransactionDetail;   // 出门开关记录
                door = result.DatabaseDetail.DoorSensorTransactionDetail; // 门磁相关记录
                software = result.DatabaseDetail.SoftwareTransactionDetail;   // 远程相关操作记录
                alarm = result.DatabaseDetail.AlarmTransactionDetail; // 报警相关记录
                system = result.DatabaseDetail.SystemTransactionDetail;   // 系统相关记录
            } else {
                Face.Transaction.Result.ReadTransactionDatabaseDetail_Result result = (Face.Transaction.Result.ReadTransactionDatabaseDetail_Result) incr;
                card = result.DatabaseDetail.CardTransactionDetail;   // 卡相关记录
                button = result.DatabaseDetail.ButtonTransactionDetail;   // 出门开关记录
                door = result.DatabaseDetail.DoorSensorTransactionDetail; // 门磁相关记录
                software = result.DatabaseDetail.SoftwareTransactionDetail;   // 远程相关操作记录
                alarm = result.DatabaseDetail.AlarmTransactionDetail; // 报警相关记录
                system = result.DatabaseDetail.SystemTransactionDetail;   // 系统相关记录
                TransactionDetail body = result.DatabaseDetail.BodyTemperatureTransactionDetail;    // 体温相关记录
                device.setBodyCapacity((int) body.DataBaseMaxSize);
                device.setBodyUsed((int) body.WriteIndex);
            }
            device.setClockCapacity((int) card.DataBaseMaxSize);
            device.setClockUsed((int) card.WriteIndex);
            device.setButtonCapacity((int) button.DataBaseMaxSize);
            device.setButtonUsed((int) button.WriteIndex);
            device.setDoorCapacity((int) door.DataBaseMaxSize);
            device.setDoorUsed((int) door.WriteIndex);
            device.setSoftwareCapacity((int) software.DataBaseMaxSize);
            device.setSoftwareUsed((int) software.WriteIndex);
            device.setAlarmCapacity((int) alarm.DataBaseMaxSize);
            device.setAlarmUsed((int) alarm.WriteIndex);
            device.setSysCapacity((int) system.DataBaseMaxSize);
            device.setSysUsed((int) system.WriteIndex);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
