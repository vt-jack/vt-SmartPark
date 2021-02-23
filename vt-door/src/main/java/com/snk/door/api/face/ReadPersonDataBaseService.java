package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.Data.Person;
import Face.Person.ReadPersonDataBase;
import Face.Person.Result.PersonDataBase_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.domain.TimeSlot;
import com.snk.door.domain.User;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceAuthService;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.ITimeSlotService;
import com.snk.door.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 读取所有用户
 */
@Component
public class ReadPersonDataBaseService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadPersonDataBaseService.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IDeviceAuthService deviceAuthService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private ITimeSlotService timeSlotService;
    @Autowired
    private ReadUserPhotoService readUserPhotoService;

    public ReadPersonDataBaseService() {
        super(DoorOperation.READ_PRESON_DATA_BASE);
    }

    /**
     * 读取所有用户
     * @param device
     */
    public void readPersonDatabase(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadPersonDataBase cmd = new ReadPersonDataBase(par);
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
            PersonDataBase_Result result = (PersonDataBase_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            Device device = deviceService.selectDeviceById(doorRsp.getId());
            if (ObjectUtils.isEmpty(device)) {
                log.warn("设备已被删除");
                return;
            }
            List<Person> personList = result.PersonList;
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setSn(doorRsp.getSn());
            Map<Integer, Long> tsMap = timeSlotService.selectTimeSlotList(timeSlot).stream().collect(Collectors.toMap(TimeSlot::getIdx, TimeSlot::getId));;
            List<Integer> userIdList = new ArrayList<>();
            for (Person item : personList) {
                String cardNo = item.CardData + "";
                User user = new User();
                user.setName(StringUtils.trim(item.PName));   // 人员姓名
                user.setUserNo(item.PCode); // 用户号
                user.setCardNo(cardNo); // 卡号
                user.setCreateBy("system");
                user.setCreateTime(DateUtils.getNowDate());
                if (item.IsFaceFeatureCode) {
                    userIdList.add(item.UserCode);
                }
                userService.insertUser(user);
                if (ObjectUtils.isEmpty(cardNo) && !item.IsFaceFeatureCode) {    // 没有卡号且没有头像不添加到权限表
                    continue;
                }
                Auth auth = new Auth();
                auth.setUserId(user.getId());
                auth.setDeviceId(device.getId());
                auth.setSn(device.getSn());
                auth.setControlPort(device.getControlPort());
                auth.setPositionId(device.getPositionId());
                auth.setCardStatus((byte) item.CardStatus);
                auth.setCardNo(cardNo);
                auth.setSlotId(tsMap.get(item.TimeGroup));
                auth.setOpenNum(item.OpenTimes);
                auth.setSpecial(0 == item.CardType ? "0" : "2");    // 1 常开  0 普通卡
                auth.setExpTime(DateUtils.getTimeByCalendar(item.Expiry));
                auth.setHoliday("0");
                auth.setStatus("1");
                auth.setDelFlag("0");
                auth.setCreateBy("system");
                auth.setCreateTime(DateUtils.getNowDate());
                deviceAuthService.insertAuth(auth);
            }
            if (!CollectionUtils.isEmpty(userIdList)) {
                readUserPhotoService.readUserPhoto(device, userIdList);
            }
            log.info("命令完成：{}, 结果：{}", getOperation().getResult(), JSON.toJSONString(result));
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}