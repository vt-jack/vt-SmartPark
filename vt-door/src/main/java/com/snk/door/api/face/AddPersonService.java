package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.Data.Person;
import Face.Person.AddPerson;
import Face.Person.Parameter.Person_Parameter;
import Face.Person.Result.Person_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传用户
 */
@Component
public class AddPersonService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(AddPersonService.class);

    @Autowired
    private IDeviceAuthService deviceAuthService;

    public AddPersonService() {
        super(DoorOperation.WRITE_CARD_LIST);
    }

    /**
     * 上传用户
     * @param device
     */
    public void addPerson(Device device, Boolean sendMsg) {
        if (paramIsEmpty(device)) {
            return;
        }
        // 本次变更
        List opAuth = (List) device.getParam();
        List<Auth> opList = new ArrayList<>();
        opAuth.stream().forEach(item -> opList.add(JSON.parseObject(JSON.toJSONString(item), Auth.class)));
        ArrayList<Person> personList = new ArrayList<>();
        for (Auth item : opList) {
            try {
                if (DeviceType.FACE.getValue().equals(device.getDeviceType())) {    // 人脸
                    if (StringUtils.isNotEmpty(item.getPhoto())) {  // 头像不为空直接continue
                        continue;
                    }
                } else if (DeviceType.FINGER.getValue().equals(device.getDeviceType())) {    // 指纹
                    if (!CollectionUtils.isEmpty(JSON.parseObject(item.getFinger(), ArrayList.class))) {   // 指纹数据不为空直接continue
                        continue;
                    }
                }
                Person person = new Person();
                person.PName = item.getUserName();
                person.UserCode = item.getUserId().intValue();
                person.PCode = item.getUserNo();
                if (StringUtils.isNotEmpty(item.getCardNo())) {
                    person.CardData = Long.valueOf(item.getCardNo());
                }
                person.OpenTimes = item.getOpenNum();
                person.CardType = "2".equals(item.getSpecial()) ? 1 : 0;    // 1 常开  0 普通卡
                if (StringUtils.isNotEmpty(item.getAdmin())) {
                    person.Identity = Integer.valueOf(item.getAdmin());
                }
                if (StringUtils.isNotEmpty(item.getDeptName())) {
                    person.Dept = item.getDeptName();
                }
                if (StringUtils.isNotEmpty(item.getPostName())) {
                    person.Job = item.getPostName();
                }
                if ("1".equals(item.getHoliday())) {    // 节假日禁止通行
                    for (int i = 1; i < 31; i++) {
                        person.SetHolidayValue(i, false);
                    }
                }
                person.Expiry = DateUtils.getCalendar(item.getExpTime());
                person.CardStatus = item.getCardStatus();
                personList.add(person);
                item.setStatus("1");
                item.setRemark(null);
            } catch (Exception e) {
                item.setRemark("上传失败：" + e.getMessage());
            }
        }
        if (CollectionUtils.isEmpty(personList)) {
            return;
        }
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), opList);
        doorRsp.setSn(device.getSn());
        doorRsp.setSendMsg(sendMsg);
        commandDetail.UserPar = doorRsp;
        Person_Parameter par = new Person_Parameter(commandDetail, personList);
        AddPerson cmd = new AddPerson(par);
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
            Person_Result result = (Person_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            ArrayList<Integer> failList = result.UserCodeList;  // 上传失败集合
            List<Auth> authList = (List) doorRsp.getParam();
            failList.stream().forEach(item -> {
                authList.stream().forEach(auth -> {
                    if (item.equals(auth.getUserId().intValue())) {
                        auth.setRemark("上传失败");
                    }
                });
            });
            deviceAuthService.insertAuth(authList);
            if (doorRsp.getSendMsg()) {
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
