package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.E_WeekDay;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import Door.Access.Door8800.Command.TimeGroup.AddTimeGroup;
import Door.Access.Door8800.Command.TimeGroup.Parameter.AddTimeGroup_Parameter;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.constant.SymbolConstants;
import com.snk.common.utils.DateUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.domain.TimeSlot;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.ITimeSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置开门时段
 */
@Component
public class WriteTimeGroupService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WriteTimeGroupService.class);

    @Autowired
    private ITimeSlotService timeSlotService;

    public WriteTimeGroupService() {
        super(DoorOperation.WRITE_TIME_GROUP);
    }

    /**
     * 设置开门时段
     * @param device
     */
    public void writeTimeGroup(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        TimeSlot timeSlot = JSON.parseObject(JSON.toJSONString(device.getParam()), TimeSlot.class);
        // 校验时段下标、开门时段不能为空
        if (ObjectUtils.isEmpty(timeSlot.getIdx())) {
            DoorRsp doorRsp = new DoorRsp(device.getId());
            doorRsp.setResult(Constants.DEFAULT_ERROR_MSG);
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.error("参数不能为空：{},SN：{}", getOperation(), device.getSn());
            return;
        }
        Map<String, List<Map<String, String>>> timeGroupMap = JSON.parseObject(timeSlot.getTimeGroup(), HashMap.class);
        ArrayList<WeekTimeGroup> list = new ArrayList<>();
        WeekTimeGroup timeGroup = new WeekTimeGroup(8, Integer.valueOf(timeSlot.getIdx().toString()));
        for (String key : timeGroupMap.keySet()) {
            String index = key.substring(4);
            E_WeekDay weekDay = null;
            switch (index) {
                case "0" :
                    weekDay = E_WeekDay.Monday;
                    break;
                case "1" :
                    weekDay = E_WeekDay.Tuesday;
                    break;
                case "2" :
                    weekDay = E_WeekDay.Wednesday;
                    break;
                case "3" :
                    weekDay = E_WeekDay.Thursday;
                    break;
                case "4" :
                    weekDay = E_WeekDay.Friday;
                    break;
                case "5" :
                    weekDay = E_WeekDay.Saturday;
                    break;
                case "6" :
                    weekDay = E_WeekDay.Sunday;
                    break;
            }
            List<Map<String, String>> timeList = timeGroupMap.get(key);
            if (!CollectionUtils.isEmpty(timeList)) {
                for (int i = 0; i < timeList.size(); i++) {
                    String[] start = timeList.get(i).get("start").split(SymbolConstants.COLON);
                    timeGroup.GetItem(weekDay).GetItem(i).SetBeginTime(Integer.valueOf(start[0]), Integer.valueOf(start[1]));
                    String[] end = timeList.get(i).get("end").split(SymbolConstants.COLON);
                    timeGroup.GetItem(weekDay).GetItem(i).SetEndTime(Integer.valueOf(end[0]), Integer.valueOf(end[1]));
                }
            }
        }
        list.add(timeGroup);
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), timeSlot);
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        AddTimeGroup_Parameter par = new AddTimeGroup_Parameter(commandDetail, list);
        AddTimeGroup cmd = new AddTimeGroup(par);
        // 添加命令到队列
        _Allocator.AddCommand(cmd);
    }

    /**
     * 当命令完成时，会触发此函数回调
     *
     * @param cmd 此次事件所关联的命令详情
     * @param result 命令包含的结果
     */
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        try {
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            doorRsp.setResult(getOperation().getResult());
            TimeSlot timeSlot = (TimeSlot) doorRsp.getParam();
            Integer idx = Integer.valueOf(timeSlot.getIdx().toString());
            if (ObjectUtils.isEmpty(JSON.parseObject(timeSlot.getTimeGroup(), HashMap.class))) { // 删除
                timeSlotService.deleteTimeSlotByIdx(doorRsp.getSn(), idx);
            } else {
                timeSlot.setCreateBy(doorRsp.getUserName());
                timeSlot.setCreateTime(DateUtils.getNowDate());
                TimeSlot ts = timeSlotService.selectTimeSlotByIdx(doorRsp.getSn(), idx);
                if (ObjectUtils.isEmpty(ts)) {  // 新增
                    timeSlotService.insertTimeSlot(timeSlot);
                } else {    // 更新
                    timeSlot.setId(ts.getId());
                    timeSlotService.updateTimeSlot(timeSlot);
                }
            }
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}
