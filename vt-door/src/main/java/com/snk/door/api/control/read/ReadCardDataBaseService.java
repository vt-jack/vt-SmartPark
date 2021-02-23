package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Card.Parameter.ReadCardDataBase_Parameter;
import Door.Access.Door8800.Command.Card.ReadCardDataBase;
import Door.Access.Door8800.Command.Card.Result.ReadCardDataBase_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.CardType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 采集卡数据
 */
@Component
public class ReadCardDataBaseService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadCardDataBaseService.class);

    public ReadCardDataBaseService() {
        super(DoorOperation.READ_CARD_DATABASE_DETAIL);
    }

    /**
     * 采集卡数据
     * @param device
     */
    public void readCardDataBase(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        ReadCardDataBase_Parameter par = new ReadCardDataBase_Parameter(commandDetail, ObjectUtils.isEmpty(device.getCardType()) ? CardType.ALL.getValue() : device.getCardType());
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        if (ModelType.COMMON.getValue().equals(device.getModelType())) { // 普通板
            ReadCardDataBase cmd = new ReadCardDataBase(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {    // 高级板
            Door.Access.Door89H.Command.Card.ReadCardDataBase cmd = new Door.Access.Door89H.Command.Card.ReadCardDataBase(par);
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
            ReadCardDataBase_Result result = (ReadCardDataBase_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            // 写入设备权限表 TODO
            log.info("命令完成：{},卡片数据库类型：{},读取到的卡片列表：{},读取到的卡片数量：{}", getOperation().getResult(),
                    result.CardType, JSON.toJSONString(result.CardList), result.DataBaseSize);
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
