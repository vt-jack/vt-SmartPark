package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Card.Parameter.WriteCardListBySequence_Parameter;
import Door.Access.Door8800.Command.Card.WriteCardListBySequence;
import Door.Access.Door8800.Command.Data.CardDetail;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.control.oper.ClearCardDataBaseService;
import com.snk.door.api.control.oper.DeleteCardService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.enums.CardStatus;
import com.snk.door.enums.CardType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import com.snk.door.service.IDeviceAuthService;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将卡片列表写入到控制器非排序区
 */
@Component
public class WriteCardListBySequenceService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WriteCardListBySequenceService.class);

    @Autowired
    private IDeviceAuthService deviceAuthService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private ClearCardDataBaseService clearCardDataBaseService;
    @Autowired
    private WriteCardListBySortService writeCardListBySortService;
    @Autowired
    private DeleteCardService deleteCardService;

    public WriteCardListBySequenceService() {
        super(null);
    }

    /**
     * 将卡片列表写入到控制器非排序区
     * @param device
     */
    public void writeCardListBySequence(Device device, DoorOperation operation, Boolean sendMsg) {
        setOperation(operation);
        if (paramIsEmpty(device)) {
            return;
        }
        // 本次变更
        List opAuth = (List) device.getParam();
        List<Auth> opList = new ArrayList<>();
        opAuth.stream().forEach(item -> {
            Auth auth = JSON.parseObject(JSON.toJSONString(item), Auth.class);
            auth.setPhoto(null);
            opList.add(auth);
        });
        // 已存在卡片
        List<Auth> authList = deviceAuthService.selectAuthBySn(device.getSn());

        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), opList);
        doorRsp.setSn(device.getSn());
        doorRsp.setSendMsg(sendMsg);
        commandDetail.UserPar = doorRsp;
        if (ModelType.COMMON.getValue().equals(device.getModelType())) {    // 普通板
            ArrayList<CardDetail> cardDetailList = new ArrayList<>();
            for (Auth item : opList) {
                try {
                    if (StringUtils.isEmpty(item.getCardNo())) {
                        item.setRemark("上传失败：卡号不能为空");
                        continue;
                    }
                    boolean isExists = false;
                    boolean bUse = "0".equals(item.getDelFlag());
                    boolean black = CardStatus.BLACKLIST.getValue().equals(item.getCardStatus());
                    // 已有权限数据
                    for (CardDetail detail : cardDetailList) {
                        if (detail.GetCardData().equals(item.getCardNo())) {    // 已存在添加门
                            detail.SetDoor(item.getControlPort(), bUse);
                            detail.SetTimeGroup(item.getControlPort(), bUse ? item.getSlotIdx() : 0);
                            isExists = true;
                            break;
                        }
                    }
                    // 不存在添加一条数据
                    if (!isExists) {
                        CardDetail cardDetail = new CardDetail();
                        // 历史权限数据
                        authList.stream().forEach(auth -> {
                            if (item.getCardNo().equals(auth.getCardNo())
                                    && item.getControlPort() != auth.getControlPort()) {    // 已存在添加门
                                cardDetail.SetDoor(auth.getControlPort(), true);
                                cardDetail.SetTimeGroup(auth.getControlPort(), auth.getSlotIdx());
                            }
                        });
                        cardDetail.SetCardData(item.getCardNo());
                        cardDetail.SetDoor(item.getControlPort(), bUse);
                        cardDetail.SetTimeGroup(item.getControlPort(), bUse ? item.getSlotIdx() : 0);
                        switch (item.getSpecial()) {
                            case "0" : cardDetail.SetNormal(); break;
                            case "1" : cardDetail.SetPrivilege(); break;
                            case "2" : cardDetail.SetTiming(); break;
                            case "3" : cardDetail.SetGuardTour(); break;
                            case "4" : cardDetail.SetAlarmSetting(); break;
                        }
                        cardDetail.OpenTimes = item.getOpenNum();
                        if ("1".equals(item.getHoliday())) {    // 节假日禁止通行
                            for (int i = 1; i < 31; i++) {
                                cardDetail.SetHolidayValue(i, false);
                            }
                        }
                        cardDetail.Expiry = DateUtils.getCalendar(item.getExpTime());
                        cardDetail.CardStatus = item.getCardStatus();
                        cardDetailList.add(cardDetail);
                    }
                    item.setStatus(!bUse && black ? "0" : "1");  // 已删除加入黑名单不改变状态
                    item.setRemark(null);
                } catch (Exception e) {
                    item.setRemark("上传失败：" + e.getMessage());
                }
            }
            WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(commandDetail, cardDetailList);
            WriteCardListBySequence cmd = new WriteCardListBySequence(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else { // 高级板
            ArrayList<Door.Access.Door89H.Command.Data.CardDetail> cardDetailList = new ArrayList<>();
            for (Auth item : opList) {
                try {
                    if (StringUtils.isEmpty(item.getCardNo())) {
                        item.setRemark("上传失败：卡号不能为空");
                        continue;
                    }
                    boolean isExists = false;
                    boolean bUse = "0".equals(item.getDelFlag());
                    boolean black = CardStatus.BLACKLIST.getValue().equals(item.getCardStatus());
                    // 已有权限数据
                    for (Door.Access.Door89H.Command.Data.CardDetail detail : cardDetailList) {
                        if (detail.GetCardData().equals(item.getCardNo())) {    // 已存在添加门
                            detail.SetDoor(item.getControlPort(), bUse);
                            detail.SetTimeGroup(item.getControlPort(), bUse ? item.getSlotIdx() : 0);
                            isExists = true;
                            break;
                        }
                    }
                    // 不存在添加一条数据
                    if (!isExists) {
                        Door.Access.Door89H.Command.Data.CardDetail cardDetail = new Door.Access.Door89H.Command.Data.CardDetail();
                        // 历史权限数据
                        authList.stream().forEach(auth -> {
                            if (item.getCardNo().equals(auth.getCardNo())
                                    && item.getControlPort() != auth.getControlPort()) {    // 已存在添加门
                                cardDetail.SetDoor(auth.getControlPort(), true);
                                cardDetail.SetTimeGroup(auth.getControlPort(), auth.getSlotIdx());
                            }
                        });
                        cardDetail.SetCardData(item.getCardNo());
                        cardDetail.SetDoor(item.getControlPort(), bUse);
                        cardDetail.SetTimeGroup(item.getControlPort(), bUse ? item.getSlotIdx() : 0);
                        switch (item.getSpecial()) {
                            case "0" : cardDetail.SetNormal(); break;
                            case "1" : cardDetail.SetPrivilege(); break;
                            case "2" : cardDetail.SetTiming(); break;
                            case "3" : cardDetail.SetGuardTour(); break;
                            case "4" : cardDetail.SetAlarmSetting(); break;
                        }
                        cardDetail.OpenTimes = item.getOpenNum();
                        if ("1".equals(item.getHoliday())) {    // 节假日禁止通行
                            for (int i = 1; i < 31; i++) {
                                cardDetail.SetHolidayValue(i, false);
                            }
                        }
                        cardDetail.Expiry = DateUtils.getCalendar(item.getExpTime());
                        cardDetail.CardStatus = item.getCardStatus();
                        cardDetailList.add(cardDetail);
                    }
                    item.setStatus(!bUse && black ? "0" : "1");  // 已删除加入黑名单不改变状态
                    item.setRemark(null);
                } catch (Exception e) {
                    item.setRemark("上传失败：" + e.getMessage());
                }
            }
            WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(commandDetail, cardDetailList);
            Door.Access.Door89H.Command.Card.WriteCardListBySequence cmd = new Door.Access.Door89H.Command.Card.WriteCardListBySequence(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        }
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
            // 更新设备权限
            List<Auth> opList = (List) doorRsp.getParam();
            deviceAuthService.insertAuth(opList);   // 新增设备权限数据
            List<Auth> authList = deviceAuthService.selectAuthBySn(doorRsp.getSn()); // 根据SN查询所有权限数据
            if (!ObjectUtils.isEmpty(authList) &&
                    authList.stream().collect(Collectors.groupingBy(auth -> auth.getSn() + auth.getUserId())).keySet().size() > 2000) {    // 超过2000张卡写入排序区
                Device device = deviceService.selectDeviceById(doorRsp.getId());
                if (!ObjectUtils.isEmpty(device)) {
                    clearCardDataBaseService.clearCardDataBase(device, false);    // 清空非排序区卡片数据
                    writeCardListBySortService.writeCardListBySort(device, getOperation(), false); // 写入排序区
                    Device upDevice = new Device();
                    upDevice.setSn(doorRsp.getSn());
                    upDevice.setCardType(CardType.SORT.getValue());
                    deviceService.updateDeviceBySn(upDevice); // 更新卡片数据库类型
                }
            } else {
                List<Auth> delList = new ArrayList<>();
                for (Auth item : opList) {  // 判断本次操作数据在数据库中是否存在，不存在需要把设备中的卡片数据删除
                    boolean isExists = false;
                    for (Auth auth : authList) {
                        if (item.getSn().equals(auth.getSn()) &&
                                item.getUserId().equals(auth.getUserId())) {
                            isExists = true;
                            break;
                        }
                    }
                    if (!isExists) {
                        delList.add(item);
                    }
                }
                if (!CollectionUtils.isEmpty(delList)) {
                    Device device = deviceService.selectDeviceById(doorRsp.getId());
                    device.setParam(delList);
                    deleteCardService.deleteCard(device);
                }
            }
            // 发送消息
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
