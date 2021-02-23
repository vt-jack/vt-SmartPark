package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Door8800.Command.System.CloseAlarm;
import Door.Access.Door8800.Command.System.Parameter.CloseAlarm_Parameter;
import com.alibaba.fastjson.JSON;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 关闭所有报警
 */
@Component
public class CloseAlarmService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(CloseAlarmService.class);

    public CloseAlarmService() {
        super(DoorOperation.CLOSE_ALARM);
    }

    /**
     * 关闭所有报警
     * @param device
     */
    public void closeAlarm(Device device) {
        //定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp(device.getId());
        CloseAlarm_Parameter par = new CloseAlarm_Parameter(commandDetail);
        CloseAlarm cmd = new CloseAlarm(par);
        //添加命令到队列
        _Allocator.AddCommand(cmd);
    }

}
