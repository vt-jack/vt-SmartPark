package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Door.Parameter.WritePushButtonSetting_Parameter;
import Door.Access.Door8800.Command.Door.WritePushButtonSetting;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.control.read.ReadRelayReleaseTimeService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 写入出门按钮功能
 */
@Component
public class WritePushButtonSettingService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadRelayReleaseTimeService.class);

    @Autowired
    private IDeviceService deviceService;

    public WritePushButtonSettingService() {
        super(DoorOperation.WRITE_PUSH_BUTTON_SETTING);
    }

    /**
     * 写入出门按钮功能
     * @param device
     */
    public void writePushButtonSetting(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        Map<String, Boolean> map = (Map) device.getParam();
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp(device.getId(), map);
        WritePushButtonSetting_Parameter par = new WritePushButtonSetting_Parameter(commandDetail, device.getControlPort(), map.get("use"), map.get("normallyOpen"));
        WritePushButtonSetting cmd = new WritePushButtonSetting(par);
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
            doorRsp.setResult(getOperation().getResult());
            // 更新出门按钮功能
            Map<String, Boolean> map = (Map) doorRsp.getParam();
            Device device = new Device();
            device.setId(doorRsp.getId());
            device.setUseButton(map.get("use"));
            device.setNormallyOpen(map.get("normallyOpen"));
            device.setUpdateBy(doorRsp.getUserName());
            device.setUpdateTime(DateUtils.getNowDate());
            deviceService.updateDevice(device);
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
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
        log.error("发生错误：{}, 命令：{}, 请求参数：{}", StringUtils.isEmpty(errorMsg) ? getOperation().getErrorMsg() : errorMsg,
                getOperation().getDescribe(), JSON.toJSONString(cmd.getCommandParameter().getCommandDetail().Connector));
    }
}
