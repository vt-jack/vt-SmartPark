package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.ReadVersion;
import Door.Access.Door8800.Command.System.Result.ReadVersion_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.StringUtils;
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
 * 读取版本号
 */
@Component
public class ReadVersionService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadVersionService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadVersionService() {
        super(DoorOperation.READ_VERSION);
    }

    /**
     * 读取版本号
     * @param device 设备信息
     */
    public void readVersion(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        if (isControl(device)) {
            ReadVersion cmd = new ReadVersion(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {
            Face.System.ReadVersion cmd = new Face.System.ReadVersion(par);
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
            // 更新设备版本号
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            String version;
            if (incr instanceof ReadVersion_Result) {
                ReadVersion_Result result = (ReadVersion_Result) incr;
                version = result.Version.trim();
            } else {
                Face.System.Result.ReadVersion_Result result = (Face.System.Result.ReadVersion_Result) incr;
                version = StringUtils.isNotEmpty(result.Version) ? result.Version.trim() : null;
                device.setVerFace(StringUtils.isNotEmpty(result.FaceVersion) ? result.FaceVersion.trim() : null);
                device.setVerFinger(StringUtils.isNotEmpty(result.FingerprintVersion) ? result.FingerprintVersion.trim() : null);
            }
            device.setVer(version);
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{},结果：{}", getOperation().getResult(), version);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
