package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.Person.DeletePerson;
import Face.Person.Parameter.DeletePerson_Parameter;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除用户
 */
@Component
public class DeletePersonService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(DeletePersonService.class);

    @Autowired
    private IDeviceAuthService deviceAuthService;

    public DeletePersonService() {
        super(DoorOperation.BAN_CARD_LIST);
    }

    /**
     * 删除用户
     * @param device
     */
    public void deletePerson(Device device, Boolean sendMsg) {
        if (paramIsEmpty(device)) {
            return;
        }
        // 本次变更
        List opAuth = (List) device.getParam();
        List<Auth> opList = new ArrayList<>();
        opAuth.stream().forEach(item -> opList.add(JSON.parseObject(JSON.toJSONString(item), Auth.class)));
        ArrayList<Integer> ucList = new ArrayList<>();
        opList.stream().forEach(item -> {
            ucList.add(item.getUserId().intValue());
            item.setStatus("1");
            item.setRemark(null);
        });
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), opList);
        doorRsp.setSn(device.getSn());
        doorRsp.setSendMsg(sendMsg);
        commandDetail.UserPar = doorRsp;
        DeletePerson_Parameter par = new DeletePerson_Parameter(commandDetail, ucList);
        DeletePerson cmd = new DeletePerson(par);
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
            List<Auth> authList = (List) doorRsp.getParam();
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
