package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Card.ReadCardDatabaseDetail;
import Door.Access.Door8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
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
 * 采集卡信息
 */
@Component
public class ReadCardDatabaseDetailService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadCardDatabaseDetailService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadCardDatabaseDetailService() {
        super(DoorOperation.READ_CARD_DATABASE_DETAIL);
    }

    /**
     * 采集卡信息
     * @param device 设备信息
     */
    public void readCardDatabaseDetail(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadCardDatabaseDetail cmd = new ReadCardDatabaseDetail(par);
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
            ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setCardCapacity((int) result.SortDataBaseSize + (int) result.SequenceDataBaseSize);
            device.setCardUsed((int) result.SortCardSize + (int) result.SequenceCardSize);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{},排序数据区容量上限：{},排序数据区已使用数量：{};顺序储存区容量上限：{},顺序储存区已使用数量：{}", getOperation().getResult(),
                    result.SortDataBaseSize, result.SortCardSize, result.SequenceDataBaseSize, result.SequenceCardSize);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
