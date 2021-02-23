package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Data.AbstractTransaction;
import Door.Access.Door8800.Command.Data.*;
import Door.Access.Door8800.Command.Transaction.Parameter.ReadTransactionDatabase_Parameter;
import Door.Access.Door8800.Command.Transaction.ReadTransactionDatabase;
import Door.Access.Door8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
import Door.Access.Door8800.Command.Transaction.e_TransactionDatabaseType;
import Face.Data.BodyTemperatureTransaction;
import com.snk.common.constant.Constants;
import com.snk.common.constant.SymbolConstants;
import com.snk.common.utils.DateUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.face.ReadFileService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.domain.User;
import com.snk.door.enums.*;
import com.snk.door.mongo.entity.CurrentRecord;
import com.snk.door.mongo.service.IMongoCurrentRecord;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.IUserService;
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
 * 读取新记录
 */
@Component
public class ReadTransactionDatabaseService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadTransactionDatabaseService.class);

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMongoCurrentRecord mongoCurrentRecord;
    @Autowired
    private ReadFileService readFileService;

    public ReadTransactionDatabaseService() {
        super(DoorOperation.READ_TRANSACTION_DATABASE);
    }

    /**
     * 读取新记录
     * @param device
     */
    public void readTransactionDatabase(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), device.getModelType());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        if (isControl(device)) {
            for (e_TransactionDatabaseType databaseType : e_TransactionDatabaseType.values()) {
                ReadTransactionDatabase_Parameter par = new ReadTransactionDatabase_Parameter(commandDetail, databaseType);
                par.Quantity = 0;
                par.PacketSize = 60;
                if (ModelType.COMMON.getValue().equals(device.getModelType())) {   // 普通板
                    ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
                    // 添加命令到队列
                    _Allocator.AddCommand(cmd);
                } else {    // 高级板
                    Door.Access.Door89H.Command.Transaction.ReadTransactionDatabase cmd = new Door.Access.Door89H.Command.Transaction.ReadTransactionDatabase(par);
                    // 添加命令到队列
                    _Allocator.AddCommand(cmd);
                }
            }
        } else {
            for (Face.Data.e_TransactionDatabaseType databaseType : Face.Data.e_TransactionDatabaseType.values()) {
                Face.Transaction.Parameter.ReadTransactionDatabase_Parameter par = new Face.Transaction.Parameter.ReadTransactionDatabase_Parameter(commandDetail, databaseType);
                Face.Transaction.ReadTransactionDatabase cmd = new Face.Transaction.ReadTransactionDatabase(par);
                // 添加命令到队列
                _Allocator.AddCommand(cmd);
            }
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
            Map<String, Object> param = new HashMap<>();
            doorRsp.setParam(param);
            param.put("result", getOperation().getResult());
            List<Device> deviceList = deviceService.selectDeviceBySn(doorRsp.getSn());
            if (CollectionUtils.isEmpty(deviceList)) {
                param.put("result", "设备已被删除");
                // 发送消息
                pushMessage(getOperation().getEvent(), getMessage(doorRsp));
                return;
            }
            List<CurrentRecord> recordList = new ArrayList<>();
            List<CurrentRecord> faceList = new ArrayList<>();
            List<CurrentRecord> fingerList = new ArrayList<>();
            if (incr instanceof ReadTransactionDatabase_Result) {
                ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) incr;
                param.put("recordType", result.DatabaseType.getValue());
                if (CollectionUtils.isEmpty(result.TransactionList)) {
                    // 发送消息
                    pushMessage(getOperation().getEvent(), getMessage(doorRsp));
                    return;
                }
                for (AbstractTransaction transaction : result.TransactionList) {
                    CurrentRecord record = new CurrentRecord();
                    record.setSn(doorRsp.getSn());
                    record.setType(result.DatabaseType.getValue());
                    record.setIoTime(DateUtils.getTimeByCalendar(transaction.TransactionDate()));
                    Short controlPort = null;
                    switch (result.DatabaseType) {
                        case OnCardTransaction: // 刷卡记录
                            String cardNo;
                            Short transactionCode;
                            Boolean isEnter, isExit;
                            if (ModelType.COMMON.getValue().equals(doorRsp.getParam().toString())) {    // 普通板
                                CardTransaction cardTransaction = (CardTransaction) transaction;
                                controlPort = cardTransaction.DoorNum();
                                cardNo = cardTransaction.CardData;
                                transactionCode = cardTransaction.TransactionCode();
                                isEnter = cardTransaction.IsEnter();
                                isExit = cardTransaction.IsExit();
                            } else {    // 高级板
                                Door.Access.Door89H.Command.Data.CardTransaction cardTransaction = (Door.Access.Door89H.Command.Data.CardTransaction) transaction;
                                controlPort = cardTransaction.DoorNum();
                                cardNo = cardTransaction.CardData;
                                transactionCode = cardTransaction.TransactionCode();
                                isEnter = cardTransaction.IsEnter();
                                isExit = cardTransaction.IsExit();
                            }
                            setUserInfo(cardNo, record);
                            record.setTransactionCode(transactionCode);
                            record.setDescribe(CardRecord.getComment(transactionCode));   // 描述
                            record.setIoType(isEnter ? "1" : isExit ? "2" : null);
                            break;
                        case OnButtonTransaction: // 出门按钮记录
                            ButtonTransaction buttonTransaction = (ButtonTransaction) transaction;
                            controlPort = buttonTransaction.Door;
                            record.setTransactionCode(buttonTransaction.TransactionCode());
                            record.setDescribe(ButtonRecord.getComment(buttonTransaction.TransactionCode()));
                            break;
                        case OnDoorSensorTransaction: // 门磁记录
                            DoorSensorTransaction doorSensorTransaction = (DoorSensorTransaction) transaction;
                            controlPort = doorSensorTransaction.Door;
                            record.setTransactionCode(doorSensorTransaction.TransactionCode());
                            record.setDescribe(DoorRecord.getComment(doorSensorTransaction.TransactionCode()));
                            break;
                        case OnSoftwareTransaction: // 软件操作记录
                            SoftwareTransaction softwareTransaction = (SoftwareTransaction) transaction;
                            controlPort = softwareTransaction.Door;
                            record.setTransactionCode(softwareTransaction.TransactionCode());
                            record.setDescribe(SoftwareRecord.getComment(softwareTransaction.TransactionCode()));
                            break;
                        case OnAlarmTransaction: // 报警记录
                            AlarmTransaction alarmTransaction = (AlarmTransaction) transaction;
                            controlPort = alarmTransaction.Door;
                            record.setTransactionCode(alarmTransaction.TransactionCode());
                            record.setDescribe(AlarmRecord.getComment(alarmTransaction.TransactionCode()));
                            break;
                        case OnSystemTransaction: // 系统记录
                            SystemTransaction systemTransaction = (SystemTransaction) transaction;
                            record.setTransactionCode(systemTransaction.TransactionCode());
                            record.setDescribe(SystemRecord.getComment(systemTransaction.TransactionCode()));
                            break;
                        default:
                            break;
                    }
                    setDeviceInfo(controlPort, deviceList, record);
                    record.setCreateTime(DateUtils.getTime());
                    recordList.add(record);
                }
                param.put("recordNum", result.Quantity + SymbolConstants.VERTICAL_LINE + result.readable);
                pushMessage(getOperation().getEvent(), getMessage(doorRsp));
                log.info("命令完成：{},记录类型：{},数量：{},剩余：{}", getOperation().getResult(), result.DatabaseType, result.Quantity, result.readable);
            } else {
                Face.Transaction.Result.ReadTransactionDatabase_Result result = (Face.Transaction.Result.ReadTransactionDatabase_Result) incr;
                param.put("recordType", result.DatabaseType.getValue());
                if (CollectionUtils.isEmpty(result.TransactionList)) {
                    // 发送消息
                    pushMessage(getOperation().getEvent(), getMessage(doorRsp));
                    return;
                }
                for (AbstractTransaction transaction : result.TransactionList) {
                    CurrentRecord record = new CurrentRecord();
                    record.setSn(doorRsp.getSn());
                    record.setType(result.DatabaseType.getValue());
                    record.setIoTime(DateUtils.getTimeByCalendar(transaction.TransactionDate()));
                    switch (result.DatabaseType) {
                        case OnCardTransaction: // 刷卡记录
                            Face.Data.CardTransaction cardTransaction = (Face.Data.CardTransaction) transaction;
                            Short transactionCode = cardTransaction.TransactionCode();
                            setUserInfo(cardTransaction.getUserCode(), record);
                            if (!CardRecordFace.isCard(transactionCode)) {
                                record.setCardNo(null);
                            }
                            if (CardRecordFace.isFace(transactionCode)) {
                                faceList.add(record);
                            }
                            if (CardRecordFace.isFinger(transactionCode)) {
                                fingerList.add(record);
                            }
                            record.setTransactionCode(transactionCode);
                            record.setDescribe(CardRecordFace.getComment(transactionCode));   // 描述
                            record.setIoType(cardTransaction.getAccessType() + "");
                            record.setSerialNumber(cardTransaction.SerialNumber);
                            break;
                        case OnDoorSensorTransaction: // 门磁记录
                            Face.Data.DoorSensorTransaction doorSensorTransaction = (Face.Data.DoorSensorTransaction) transaction;
                            record.setTransactionCode(doorSensorTransaction.TransactionCode());
                            record.setDescribe(DoorRecord.getComment(doorSensorTransaction.TransactionCode()));
                            record.setSerialNumber(doorSensorTransaction.SerialNumber);
                            break;
                        case OnSystemTransaction: // 系统记录
                            Face.Data.SystemTransaction systemTransaction = (Face.Data.SystemTransaction) transaction;
                            record.setTransactionCode(systemTransaction.TransactionCode());
                            record.setDescribe(SystemRecordFace.getComment(systemTransaction.TransactionCode()));
                            record.setSerialNumber(systemTransaction.SerialNumber);
                            break;
                        case OnBodyTemperatureTransaction: // 体温记录
                            BodyTemperatureTransaction bodyTemperatureTransaction = (BodyTemperatureTransaction) transaction;
                            record.setTransactionCode(bodyTemperatureTransaction.TransactionCode());
                            record.setTemperature(bodyTemperatureTransaction.getBodyTemperature()/10 + "");
                            record.setSerialNumber(bodyTemperatureTransaction.SerialNumber);
                            break;
                        default:
                            break;
                    }
                    setDeviceInfo(Short.valueOf("1"), deviceList, record);
                    record.setCreateTime(DateUtils.getTime());
                    recordList.add(record);
                }
                param.put("recordNum", result.Quantity + SymbolConstants.VERTICAL_LINE + result.readable);
                pushMessage(getOperation().getEvent(), getMessage(doorRsp));
                log.info("命令完成：{},记录类型：{},数量：{},剩余：{}", getOperation().getResult(), result.DatabaseType, result.Quantity, result.readable);
            }
            mongoCurrentRecord.addBatch(recordList);
            if (!CollectionUtils.isEmpty(faceList)) {
                readFileService.readFile(deviceList.get(0), faceList, 3);
            }
            if (!CollectionUtils.isEmpty(fingerList)) {
                readFileService.readFile(deviceList.get(0), fingerList, 2);
            }
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

    /**
     * 补充用户信息
     * @param cardNo
     * @param record
     */
    private void setUserInfo(String cardNo, CurrentRecord record) {
        this.setUserInfo(record, userService.selectUserByCardNo(cardNo));
    }

    /**
     * 补充用户信息
     * @param userId
     * @param record
     */
    private void setUserInfo(Long userId, CurrentRecord record) {
        this.setUserInfo(record, userService.selectUserById(userId));
    }

    private void setUserInfo(CurrentRecord record, User user) {
        if (!ObjectUtils.isEmpty(user)) {
            record.setCardNo(user.getCardNo()); // 卡号
            record.setUserId(user.getId()); // 人员ID
            record.setUserName(user.getName()); // 人员姓名
            record.setDeptId(user.getDeptId()); // 部门ID
            record.setDeptName(user.getDeptName()); // 所属部门
            record.setPostId(user.getPostId()); // 岗位ID
            record.setPostName(user.getPostName()); // 岗位
        }
    }

    /**
     * 补充设备信息
     * @param controlPort
     * @param deviceList
     * @param record
     */
    private void setDeviceInfo(Short controlPort, List<Device> deviceList, CurrentRecord record) {
        if (ObjectUtils.isEmpty(controlPort)) {
            return;
        }
        for (Device device : deviceList) {
            if (Integer.valueOf(controlPort).equals(device.getControlPort())) {
                record.setDeviceId(device.getId());
                record.setDeviceName(device.getName());
                record.setPositionId(device.getPositionId());
                record.setPositionName(device.getPositionName());
            }
        }
    }

}
