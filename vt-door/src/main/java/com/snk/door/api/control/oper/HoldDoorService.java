package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Door8800.Command.Door.HoldDoor;
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
 * 保持门常开
 */
@Component
public class HoldDoorService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(HoldDoorService.class);

    public HoldDoorService() {
        super(DoorOperation.HOLD_DOOR);
    }

    /**
     * 保持门常开
     * @param deviceList
     */
    public void holdDoor(List<Device> deviceList) {
        deviceList.stream().forEach(device -> {
            //定义命令参数
            CommandDetail commandDetail = getCommandDetail(device);
            commandDetail.UserPar = new DoorRsp(device.getId());
            RemoteDoor_Parameter par = new RemoteDoor_Parameter(commandDetail);
            par.Door.SetDoor(device.getControlPort(), 1);
            if (isControl(device)) {
                HoldDoor cmd = new HoldDoor(par);
                //添加命令到队列
                _Allocator.AddCommand(cmd);
            } else {
                Face.Door.HoldDoor cmd = new Face.Door.HoldDoor(par);
                //添加命令到队列
                _Allocator.AddCommand(cmd);
            }
        });
    }

}
