package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.ReadSN;
import Door.Access.Door8800.Command.System.Result.ReadSN_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.WorkState;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 读取SN
 */
@Component
public class ReadSNService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadSNService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadSNService() {
        super(DoorOperation.READ_SN);
    }

    /**
     * 读取SN
     * @param device 设备信息
     */
    public void readSn(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp();
        doorRsp.setParam(device.getWorkState());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadSN cmd = new ReadSN(par);
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
            ReadSN_Result result = (ReadSN_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            if (!ObjectUtils.isEmpty(doorRsp.getParam()) &&
                    WorkState.OFF_LINE.getValue().equals(doorRsp.getParam().toString())) {
                this.updateWorkState(doorRsp.getSn(), WorkState.ON_LINE.getValue());
            }
            log.info("命令完成：{},结果：{},设备在线", getOperation().getResult(), result.SN);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

    /**
     * 发生错误
     * @param cmd
     * @param errorMsg
     */
    @Override
    public void error(INCommand cmd, String errorMsg) {
        DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
        if (!ObjectUtils.isEmpty(doorRsp.getParam()) &&
                WorkState.ON_LINE.getValue().equals(doorRsp.getParam().toString())) {
            this.updateWorkState(doorRsp.getSn(), WorkState.OFF_LINE.getValue());
        }
        log.error("发生错误：{}, 命令：{}, 请求参数：{}", StringUtils.isEmpty(errorMsg) ? getOperation().getErrorMsg() : errorMsg,
                getOperation().getDescribe(), JSON.toJSONString(cmd.getCommandParameter().getCommandDetail().Connector));
    }

    /**
     * 更新设备为离线
     * @param sn
     */
    private void updateWorkState(String sn, String workState) {
        Device device = new Device();
        device.setSn(sn);
        device.setWorkState(workState);
        deviceService.updateDeviceBySn(device);
    }

}
