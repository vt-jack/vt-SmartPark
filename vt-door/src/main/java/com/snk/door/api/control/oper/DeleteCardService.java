package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Door8800.Command.Card.DeleteCard;
import Door.Access.Door8800.Command.Card.Parameter.DeleteCard_Parameter;
import com.alibaba.fastjson.JSON;
import com.snk.door.api.ControlService;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 删除卡片
 */
@Component
public class DeleteCardService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(DeleteCardService.class);

    public DeleteCardService() {
        super(DoorOperation.DELETE_CARD);
    }

    /**
     * 删除卡片
     */
    public void deleteCard(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        List<Auth> delList = (List) device.getParam();
        Set<String> cardNoSet = delList.stream().map(Auth::getCardNo).collect(Collectors.toSet());
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DeleteCard_Parameter par = new DeleteCard_Parameter(commandDetail, cardNoSet.toArray(new String[cardNoSet.size()]));
        if (ModelType.COMMON.getValue().equals(device.getModelType())) { // 普通板
            DeleteCard cmd = new DeleteCard(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {    // 高级板
            Door.Access.Door89H.Command.Card.DeleteCard cmd = new Door.Access.Door89H.Command.Card.DeleteCard(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        }
    }

}
