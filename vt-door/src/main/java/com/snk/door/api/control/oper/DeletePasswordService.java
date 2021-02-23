package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Password.DeletePassword;
import Door.Access.Door8800.Command.Password.Parameter.DeletePassword_Parameter;
import com.alibaba.fastjson.JSON;
import com.snk.door.api.ControlService;
import com.snk.door.domain.Device;
import com.snk.door.domain.Pwd;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 删除开门密码
 */
@Component
public class DeletePasswordService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(DeletePasswordService.class);

    public DeletePasswordService() {
        super(DoorOperation.DELETE_PASSWORD);
    }

    /**
     * 删除开门密码
     */
    public void deletePassword(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        List<Pwd> opList = (List) device.getParam();
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        //commandDetail.UserPar = new DoorRsp(device.getId());
        if (ModelType.COMMON.getValue().equals(device.getModelType())) {    // 普通版
            ArrayList<PasswordDetail> pwdList = new ArrayList<>();
            opList.stream().forEach(item -> {
                boolean isExists = false;
                for (PasswordDetail pwd : pwdList) {
                    if (pwd.Password.equals(item.getPassword())) {
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    PasswordDetail detail = new PasswordDetail();
                    detail.Password = item.getPassword();
                    pwdList.add(detail);
                }
            });
            DeletePassword_Parameter par = new DeletePassword_Parameter(commandDetail, pwdList);
            DeletePassword cmd = new DeletePassword(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {    // 高级版
            Set<String> pwdList = new HashSet<>();
            opList.stream().forEach(item -> {
                if (!pwdList.contains(item.getPassword())) {
                    pwdList.add(item.getPassword());
                }
            });
            Door.Access.Door89H.Command.Password.Parameter.DeletePassword_Parameter par =
                    new Door.Access.Door89H.Command.Password.Parameter.DeletePassword_Parameter(commandDetail, pwdList.toArray(new String[pwdList.size()]));
            Door.Access.Door89H.Command.Password.DeletePassword cmd = new Door.Access.Door89H.Command.Password.DeletePassword(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        }
    }

}
