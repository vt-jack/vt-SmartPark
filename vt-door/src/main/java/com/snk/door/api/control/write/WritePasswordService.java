package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Password.Parameter.WritePassword_Parameter;
import Door.Access.Door8800.Command.Password.Result.WritePassword_Result;
import Door.Access.Door8800.Command.Password.WritePassword;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.control.oper.DeletePasswordService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.domain.Pwd;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import com.snk.door.service.IDevicePwdService;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 写入开门密码
 */
@Component
public class WritePasswordService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WritePasswordService.class);

    @Autowired
    private IDevicePwdService devicePwdService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private DeletePasswordService deletePasswordService;

    public WritePasswordService() {
        super(null);
    }

    /**
     * 写入开门密码
     * @param device
     */
    public void writePassword(Device device, DoorOperation operation) {
        setOperation(operation);
        if (paramIsEmpty(device)) {
            return;
        }
        List pwdMl = (List) device.getParam();
        List<Pwd> opList = new ArrayList<>();
        pwdMl.stream().forEach(item -> opList.add(JSON.parseObject(JSON.toJSONString(item), Pwd.class)));
        List<Pwd> existsList = devicePwdService.selectPwdBySn(device.getSn()); // 根据SN查已存在的数据
        List<Pwd> pwdList = new ArrayList<>();
        for (Pwd pwd : opList) {    // 讲已存在的数据放入，sdk同一个密码会覆盖数据
            for (Pwd item : existsList) {
                if (item.getPassword().equals(pwd.getPassword()) &&
                        item.getSn().equals(pwd.getSn()) && item.getDeviceId() != pwd.getDeviceId()) {
                    pwdList.add(item);
                }
            }
        }
        pwdList.addAll(opList);
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), opList);
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        if (ModelType.COMMON.getValue().equals(device.getModelType())) {    // 普通版
            ArrayList<PasswordDetail> list = new ArrayList<>();
            for (Pwd pwd : pwdList) {
                boolean bUse = "0".equals(pwd.getDelFlag());
                boolean isExists = false;
                for (PasswordDetail detail : list) {
                    if (detail.Password.equals(pwd.getPassword())) {    // 已存在设置门即可
                        detail.SetDoor(pwd.getControlPort(), bUse);
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    PasswordDetail detail = new PasswordDetail();
                    detail.Password = pwd.getPassword();
                    detail.SetDoor(pwd.getControlPort(), bUse);
                    list.add(detail);
                }
            }
            WritePassword_Parameter par = new WritePassword_Parameter(commandDetail, list);
            WritePassword cmd = new WritePassword(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {    // 高级版
            ArrayList<Door.Access.Door89H.Command.Data.PasswordDetail> list = new ArrayList<>();
            for (Pwd pwd : pwdList) {
                boolean bUse = "0".equals(pwd.getDelFlag());
                boolean isExists = false;
                for (Door.Access.Door89H.Command.Data.PasswordDetail detail : list) {
                    if (detail.Password.equals(pwd.getPassword())) {    // 已存在设置门即可
                        detail.SetDoor(pwd.getControlPort(), bUse);
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    Door.Access.Door89H.Command.Data.PasswordDetail detail = new Door.Access.Door89H.Command.Data.PasswordDetail();
                    detail.Password = pwd.getPassword();
                    detail.SetDoor(pwd.getControlPort(), bUse);
                    detail.OpenTimes = ObjectUtils.isEmpty(pwd.getOpenTimes()) ? 65535 : pwd.getOpenTimes();
                    try {
                        detail.Expiry = DateUtils.getCalendar(StringUtils.isEmpty(pwd.getExpiry()) ? "2089-12-31 23:59:59" : pwd.getExpiry());
                    } catch (ParseException e) {
                        log.error("类型转换异常...");
                        continue;
                    }
                    list.add(detail);
                }
            }
            Door.Access.Door89H.Command.Password.Parameter.WritePassword_Parameter par = new Door.Access.Door89H.Command.Password.Parameter.WritePassword_Parameter(commandDetail, list);
            Door.Access.Door89H.Command.Password.WritePassword cmd = new Door.Access.Door89H.Command.Password.WritePassword(par);
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
            WritePassword_Result result = (WritePassword_Result) incr;
            List<Pwd> opList = (List) doorRsp.getParam();
            opList.stream().forEach(item -> {
                boolean isError = false;
                if (!ObjectUtils.isEmpty(result)) {
                    for (PasswordDetail pwd : result.ErrorDetails) {
                        if (pwd.Password.equals(item.getPassword())) {
                            isError = true;
                            break;
                        }
                    }
                }
                item.setResult(isError ? getOperation().getErrorMsg() : getOperation().getResult());
            });
            // 把写入失败的数据排除，剩下的写入DB
            List<Pwd> successList = opList.stream().filter(item -> !item.getResult().equals(getOperation().getErrorMsg())).collect(Collectors.toList());
            devicePwdService.insertPwd(successList);
            // 需要检查删除控制器上面的无效密码
            List<Pwd> delList = new ArrayList<>();
            List<Pwd> pwdList = devicePwdService.selectPwdBySn(doorRsp.getSn());
            for (Pwd pwd : successList) {
                boolean isExists = false;
                for (Pwd item : pwdList) {
                    if (item.getPassword().equals(pwd.getPassword()) &&
                        item.getSn().equals(pwd.getSn())) {
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    delList.add(pwd);
                }
            }
            if (!CollectionUtils.isEmpty(delList)) {
                Device device = deviceService.selectDeviceById(doorRsp.getId());
                device.setParam(delList);
                deletePasswordService.deletePassword(device);
            }
            doorRsp.setParam(opList);   // 覆盖doorRsp参数
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}
