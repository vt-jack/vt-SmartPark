package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.System.ReadOEM;
import Face.System.Result.OEM_Result;
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
 * 读取OEM
 */
@Component
public class ReadOEMService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadOEMService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadOEMService() {
        super(DoorOperation.READ_OEM);
    }

    /**
     * 读取OEM
     * @param device
     */
    public void readOEM(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadOEM cmd = new ReadOEM(par);
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
            OEM_Result result = (OEM_Result) incr;
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setDelivery(DateUtils.getTimeByCalendar(result.detail.DeliveryDate));
            device.setManufacturer(result.detail.Manufacturer);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
