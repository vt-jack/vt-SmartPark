package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Door8800.Command.Door.CloseDoor;
import Door.Access.Door8800.Command.Door.Parameter.RemoteDoor_Parameter;
import com.alibaba.fastjson.JSON;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 关门
 */
@Component
public class CloseDoorService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(CloseDoorService.class);

    public CloseDoorService() {
        super(DoorOperation.CLOSE_DOOR);
    }

    /**
     * 关门
     * @param deviceList
     */
    public void closeDoor(List<Device> deviceList) {
        deviceList.stream().forEach(device -> {
            //定义命令参数
            CommandDetail commandDetail = getCommandDetail(device);
            commandDetail.UserPar = new DoorRsp(device.getId());
            RemoteDoor_Parameter par = new RemoteDoor_Parameter(commandDetail);
            par.Door.SetDoor(device.getControlPort(), 1);
            if (isControl(device)) {
                CloseDoor cmd = new CloseDoor(par);
                //添加命令到队列
                _Allocator.AddCommand(cmd);
            } else {
                Face.Door.CloseDoor cmd = new Face.Door.CloseDoor(par);
                //添加命令到队列
                _Allocator.AddCommand(cmd);
            }
        });
    }

}
