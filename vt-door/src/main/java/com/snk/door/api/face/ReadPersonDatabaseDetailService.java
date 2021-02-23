package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.Person.ReadPersonDatabaseDetail;
import Face.Person.Result.ReadPersonDatabaseDetail_Result;
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
 * 读取人员存储详情
 */
@Component
public class ReadPersonDatabaseDetailService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadPersonDatabaseDetailService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadPersonDatabaseDetailService() {
        super(DoorOperation.READ_PRESON_DATA_BASE_DETAIL);
    }

    /**
     * 读取人员存储详情
     * @param device
     */
    public void readPersonDatabaseDetail(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadPersonDatabaseDetail cmd = new ReadPersonDatabaseDetail(par);
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
            ReadPersonDatabaseDetail_Result result = (ReadPersonDatabaseDetail_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setUserCapacity((int) result.SortDataBaseSize);
            device.setUserUsed((int) result.SortPersonSize);
            device.setFaceCapacity((int) result.SortFaceDataBaseSize);
            device.setFaceUsed((int) result.SortFaceSize);
            device.setFpCapacity((int) result.SortFingerprintDataBaseSize);
            device.setFpUsed((int) result.SortFingerprintSize);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{}, 结果：{}", getOperation().getResult(), JSON.toJSONString(result));
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}
