package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.AdditionalData.Parameter.ReadFile_Parameter;
import Face.AdditionalData.ReadFile;
import Face.AdditionalData.Result.ReadFile_Result;
import Face.Data.CardTransaction;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.constant.SymbolConstants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.mongo.entity.CurrentRecord;
import com.snk.door.socketio.ISocketIOService;
import com.snk.door.socketio.entity.Message;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 读取监控图片
 */
@Component
public class ReadWatchFileService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadWatchFileService.class);

    @Autowired
    private ISocketIOService socketIOService;

    public ReadWatchFileService() {
        super(DoorOperation.READ_FILE);
    }

    /**
     * 读取人脸图片
     * @param device
     */
    public void readFile(Device device, CardTransaction cardTransaction, Integer type) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), type + SymbolConstants.COMMA + cardTransaction.getRecordSerialNumber());
        commandDetail.UserPar = doorRsp;
        ReadFile_Parameter par = new ReadFile_Parameter(commandDetail, (int) cardTransaction.getRecordSerialNumber(), type, 1);
        ReadFile cmd = new ReadFile(par);
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
            ReadFile_Result result = (ReadFile_Result) incr;
            String[] params = doorRsp.getParam().toString().split(SymbolConstants.COMMA);
            CurrentRecord record = new CurrentRecord();
            record.setDeviceId(doorRsp.getId());
            record.setSerialNumber(Integer.valueOf(params[1]));
            if ("3".equals(params[0])) {
                record.setFace(Base64.encodeBase64String(result.FileDatas));
            } else if ("2".equals(params[0])) {
                record.setFinger(Base64.encodeBase64String(result.FileDatas));
            }
            Message message = new Message(null);
            message.setContent(JSON.toJSONString(record));
            socketIOService.pushMessage(Message.WATCH_DEVICE, message);
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
