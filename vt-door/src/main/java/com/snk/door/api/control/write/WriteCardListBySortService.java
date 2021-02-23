package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Card.Parameter.WriteCardListBySort_Parameter;
import Door.Access.Door8800.Command.Card.WriteCardListBySort;
import Door.Access.Door8800.Command.Data.CardDetail;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import com.snk.door.service.IDeviceAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将卡片列表写入到控制器排序区
 */
@Component
public class WriteCardListBySortService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WriteCardListBySortService.class);

    @Autowired
    private IDeviceAuthService deviceAuthService;

    public WriteCardListBySortService() {
        super(null);
    }

    /**
     * 将卡片列表写入到控制器排序区
     * *** 注意：每次都要全量上传数据，有些添加了权限没有上传的数据也会被上传到设备。无法避免
     * @param device
     */
    public void writeCardListBySort(Device device, DoorOperation operation, Boolean sendMsg) {
        setOperation(operation);
        List opAuth = (List) device.getParam();  // 本次新增
        List<Auth> opList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(opAuth)) {
            opAuth.stream().forEach(item -> {
                Auth auth = JSON.parseObject(JSON.toJSONString(item), Auth.class);
                auth.setPhoto(null);
                opList.add(auth);
            });
        }
        // 已存在卡片
        List<Auth> useList = deviceAuthService.selectAuthBySn(device.getSn());
        // 已存在的卡片信息直接覆盖重新上传
        for (Auth item : opList) {
            if (StringUtils.isEmpty(item.getCardNo())) {
                continue;
            }
            for (Auth auth : useList) {
                if (item.getCardNo().equals(auth.getCardNo()) &&
                        item.getControlPort().equals(auth.getControlPort())) {
                    useList.remove(auth);
                    break;
                }
            }
            useList.add(item);
        }
        // 排序
        List<Auth> authList = useList.stream().sorted(Comparator.comparing(Auth::getCardNo)).collect(Collectors.toList());
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), authList);
        doorRsp.setSn(device.getSn());
        doorRsp.setSendMsg(sendMsg);
        commandDetail.UserPar = doorRsp;

        if (ModelType.COMMON.getValue().equals(device.getModelType())) {    // 普通板
            ArrayList<CardDetail> cardDetailList = new ArrayList<>();
            for (Auth item : authList) {
                try {
                    if (StringUtils.isEmpty(item.getCardNo())) {
                        item.setRemark("上传失败：卡号不能为空");
                        continue;
                    }
                    boolean isExists = false;
                    boolean bUse = "0".equals(item.getDelFlag());   // 未删除
                    boolean delNoUpload = "1".equals(item.getDelFlag()) && "0".equals(item.getStatus());    // 删除未上传
                    // 已有权限数据
                    for (CardDetail detail : cardDetailList) {
                        if (detail.GetCardData().equals(item.getCardNo())) {    // 已存在添加门
                            detail.SetDoor(item.getControlPort(), bUse || delNoUpload);
                            detail.SetTimeGroup(item.getControlPort(), bUse || delNoUpload ? item.getSlotIdx() : 0);
                            isExists = true;
                            break;
                        }
                    }
                    // 不存在添加一条数据
                    if (!isExists) {
                        CardDetail cardDetail = new CardDetail();
                        cardDetail.SetDoor(item.getControlPort(), bUse || delNoUpload);
                        cardDetail.SetCardData(item.getCardNo());
                        cardDetail.SetTimeGroup(item.getControlPort(), bUse || delNoUpload ? item.getSlotIdx() : 0);
                        switch (item.getSpecial()) {
                            case "0":
                                cardDetail.SetNormal();
                                break;
                            case "1":
                                cardDetail.SetPrivilege();
                                break;
                            case "2":
                                cardDetail.SetTiming();
                                break;
                            case "3":
                                cardDetail.SetGuardTour();
                                break;
                            case "4":
                                cardDetail.SetAlarmSetting();
                                break;
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
                    item.setStatus(delNoUpload ? "0" : "1");
                    item.setRemark(null);
                } catch (Exception e) {
                    item.setRemark("上传失败：" + e.getMessage());
                }
            }
            WriteCardListBySort_Parameter par = new WriteCardListBySort_Parameter(commandDetail, cardDetailList);
            WriteCardListBySort cmd = new WriteCardListBySort(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        } else {    // 高级板
            ArrayList<Door.Access.Door89H.Command.Data.CardDetail> cardDetailList = new ArrayList<>();
            for (Auth item : authList) {
                try {
                    if (StringUtils.isEmpty(item.getCardNo())) {
                        item.setRemark("上传失败：卡号不能为空");
                        continue;
                    }
                    boolean isExists = false;
                    boolean bUse = "0".equals(item.getDelFlag());   // 未删除
                    boolean delNoUpload = "1".equals(item.getDelFlag()) && "0".equals(item.getStatus());    // 删除未上传
                    // 已有权限数据
                    for (Door.Access.Door89H.Command.Data.CardDetail detail : cardDetailList) {
                        if (detail.GetCardData().equals(item.getCardNo())) {    // 已存在添加门
                            detail.SetDoor(item.getControlPort(), bUse || delNoUpload);
                            detail.SetTimeGroup(item.getControlPort(), bUse || delNoUpload ? item.getSlotIdx() : 0);
                            isExists = true;
                            break;
                        }
                    }
                    // 不存在添加一条数据
                    if (!isExists) {
                        Door.Access.Door89H.Command.Data.CardDetail cardDetail = new Door.Access.Door89H.Command.Data.CardDetail();
                        cardDetail.SetDoor(item.getControlPort(), bUse || delNoUpload);
                        cardDetail.SetCardData(item.getCardNo());
                        cardDetail.SetTimeGroup(item.getControlPort(), bUse || delNoUpload ? item.getSlotIdx() : 0);
                        switch (item.getSpecial()) {
                            case "0":
                                cardDetail.SetNormal();
                                break;
                            case "1":
                                cardDetail.SetPrivilege();
                                break;
                            case "2":
                                cardDetail.SetTiming();
                                break;
                            case "3":
                                cardDetail.SetGuardTour();
                                break;
                            case "4":
                                cardDetail.SetAlarmSetting();
                                break;
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
                    item.setStatus(delNoUpload ? "0" : "1");
                    item.setRemark(null);
                } catch (Exception e) {
                    item.setRemark("上传失败：" + e.getMessage());
                }
            }
            WriteCardListBySort_Parameter par = new WriteCardListBySort_Parameter(commandDetail, cardDetailList);
            Door.Access.Door89H.Command.Card.WriteCardListBySort cmd = new Door.Access.Door89H.Command.Card.WriteCardListBySort(par);
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
            deviceAuthService.insertAuth(opList);
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
