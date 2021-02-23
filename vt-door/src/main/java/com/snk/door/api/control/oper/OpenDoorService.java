package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Door8800.Command.Door.OpenDoor;
import Door.Access.Door8800.Command.Door.Parameter.OpenDoor_Parameter;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.DoorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 开门
 */
@Component
public class OpenDoorService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(OpenDoorService.class);

    public OpenDoorService() {
        super(DoorOperation.OPEN_DOOR);
    }

    /**
     * 开门
     * @param deviceList
     */
    public void openDoor(List<Device> deviceList) {
        deviceList.stream().forEach(device -> {
            DoorRsp doorRsp = new DoorRsp(device.getId());
            CommandDetail commandDetail = getCommandDetail(device);
            commandDetail.UserPar = doorRsp;
            OpenDoor_Parameter par = new OpenDoor_Parameter(commandDetail);
            par.Door.SetDoor(device.getControlPort(), 1);
            if (DeviceType.CONTROL.getValue().equals(device.getDeviceType())) {
                OpenDoor cmd = new OpenDoor(par);
                // 添加命令到队列
                _Allocator.AddCommand(cmd);
            } else {
                Face.Door.OpenDoor cmd = new Face.Door.OpenDoor(par);
                // 添加命令到队列
                _Allocator.AddCommand(cmd);
            }
        });
    }

}
