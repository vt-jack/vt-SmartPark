package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.ReadSN;
import Door.Access.Door8800.Command.System.Result.ReadSN_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.constant.UserConstants;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.utils.CacheUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.domain.Model;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.IModelService;
import com.snk.door.socketio.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试连接
 */
@Component
public class TestConnectService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(TestConnectService.class);

    @Autowired
    private IModelService modelService;
    @Autowired
    private IDeviceService deviceService;

    public TestConnectService() {
        super(DoorOperation.TEST_CONNECT);
    }

    /**
     * 测试连接
     * @param device
     */
    public void testConnect(Device device) {
        if (ObjectUtils.isEmpty(CacheUtils.get(CacheUtils.DOOR_CONNECT_CACHE, device.getSn()))) {
            Map<String, Object> map = new HashMap<>();
            map.put(AjaxResult.CODE_TAG, AjaxResult.Type.ERROR);
            map.put(AjaxResult.MSG_TAG, "无法连接到设备");
            Message message = new Message(new DoorRsp().getUserId());
            message.setContent(JSON.toJSONString(map));
            pushMessage(getOperation().getEvent(), message);
            return;
        }
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        commandDetail.UserPar = new DoorRsp();
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
            // 发送给前端的消息体
            Message message = new Message(doorRsp.getUserId());
            // 返回数据
            Map<String, Object> map = new HashMap<>();
            Device device = new Device();
            device.setSn(result.SN);
            if (UserConstants.EXCEPTION.equals(deviceService.checkUnique(device))) {
                map.put(AjaxResult.CODE_TAG, AjaxResult.Type.ERROR.value());
                map.put(AjaxResult.MSG_TAG, "设备已存在");
                message.setContent(JSON.toJSONString(map));
                pushMessage(getOperation().getEvent(), message);
                return;
            }
            Model model = modelService.selectModelByModel(result.SN.substring(0, 8));
            if (ObjectUtils.isEmpty(model)) {
                map.put(AjaxResult.CODE_TAG, AjaxResult.Type.ERROR.value());
                map.put(AjaxResult.MSG_TAG, "请先配置设备型号");
                message.setContent(JSON.toJSONString(map));
                pushMessage(getOperation().getEvent(), message);
                return;
            }
            Map<String, String> data = new HashMap<>();
            data.put("sn",result.SN);
            data.put("modelPort", model.getPort());
            data.put("model", model.getModel());
            data.put("deviceType", model.getDeviceType());
            data.put("comment", model.getComment());
            data.put("image", model.getImage());
            map.put(AjaxResult.CODE_TAG, AjaxResult.Type.SUCCESS.value());
            map.put(AjaxResult.DATA_TAG, data);
            message.setContent(JSON.toJSONString(map));
            pushMessage(getOperation().getEvent(), message);
            log.info("命令完成：{},结果：{}", getOperation().getResult(), result.SN);
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
        Map<String, Object> map = new HashMap<>();
        map.put(AjaxResult.CODE_TAG, AjaxResult.Type.ERROR);
        map.put(AjaxResult.MSG_TAG, StringUtils.isEmpty(errorMsg) ? getOperation().getErrorMsg() : errorMsg);
        Message message = new Message(doorRsp.getUserId());
        message.setContent(JSON.toJSONString(map));
        pushMessage(getOperation().getEvent(), message);
        log.error("发生错误：{}, 命令：{}, 请求参数：{}", StringUtils.isEmpty(errorMsg) ? getOperation().getErrorMsg() : errorMsg,
                getOperation().getDescribe(), JSON.toJSONString(cmd.getCommandParameter().getCommandDetail().Connector));
    }
}
