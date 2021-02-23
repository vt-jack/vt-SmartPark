package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import Door.Access.Door8800.Command.TimeGroup.ReadTimeGroup;
import Door.Access.Door8800.Command.TimeGroup.Result.ReadTimeGroup_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 读取开门时间段
 */
@Component
public class ReadTimeGroupService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadTimeGroupService.class);

    public ReadTimeGroupService() {
        super(DoorOperation.READ_TIME_GROUP);
    }

    /**
     * 读取开门时间段
     * @param device
     */
    public void readTimeGroup(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        CommandParameter par = new CommandParameter(commandDetail);
        ReadTimeGroup cmd = new ReadTimeGroup(par);
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
            ReadTimeGroup_Result result = (ReadTimeGroup_Result) incr;
            ArrayList<WeekTimeGroup> list = result.List;
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
