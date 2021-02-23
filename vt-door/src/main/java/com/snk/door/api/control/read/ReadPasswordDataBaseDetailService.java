package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Password.ReadPasswordDataBaseDetail;
import Door.Access.Door8800.Command.Password.Result.ReadPasswordDataBaseDetail_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 读取开门密码详情
 */
@Component
public class ReadPasswordDataBaseDetailService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadPasswordDataBaseDetailService.class);

    public ReadPasswordDataBaseDetailService() {
        super(DoorOperation.READ_PASSWORD_DATA_BASE_DETAIL);
    }

    /**
     * 读取开门密码详情
     * @param device 设备信息
     */
    public void readPasswordDataBaseDetail(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadPasswordDataBaseDetail cmd = new ReadPasswordDataBaseDetail(par);
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
            ReadPasswordDataBaseDetail_Result result = (ReadPasswordDataBaseDetail_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            log.info("命令完成：{},结果：密码容量：{},已存数量：{}", getOperation().getResult(), result.Capacity, result.UseNumber);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
