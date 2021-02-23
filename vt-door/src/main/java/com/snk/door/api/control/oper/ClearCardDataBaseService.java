package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Card.ClearCardDataBase;
import Door.Access.Door8800.Command.Card.Parameter.ClearCardDataBase_Parameter;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.CardType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceAuthService;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 清空卡片数据库
 */
@Component
public class ClearCardDataBaseService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ClearCardDataBaseService.class);

    @Autowired
    private IDeviceAuthService deviceAuthService;
    @Autowired
    private IDeviceService deviceService;

    public ClearCardDataBaseService() {
        super(DoorOperation.CLEAR_CARD_DATABASE);
    }

    /**
     * 清空卡片数据库
     */
    public void clearCardDataBase(Device device, Boolean sendMsg) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        doorRsp.setSendMsg(sendMsg);
        commandDetail.UserPar = doorRsp;
        ClearCardDataBase_Parameter par = new ClearCardDataBase_Parameter(commandDetail, ObjectUtils.isEmpty(device.getCardType()) ? CardType.ALL.getValue() : device.getCardType());
        ClearCardDataBase cmd = new ClearCardDataBase(par);
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
            // 有可能存在非排序区往排序区转的清空，这时候不需要删除DB数据，只需要清空控制板的卡片
            if (doorRsp.getSendMsg()) {
                deviceAuthService.deleteAuthBySn(doorRsp.getSn());
                Device device = new Device();
                device.setSn(doorRsp.getSn());
                device.setCardType(CardType.NOT_SORT.getValue());
                deviceService.updateDeviceBySn(device);
                // 发送消息
                doorRsp.setResult(getOperation().getResult());
                pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            }
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
