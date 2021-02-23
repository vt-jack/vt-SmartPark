package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.Data.IdentificationData;
import Face.Data.Person;
import Face.Person.AddPersonAndImage;
import Face.Person.Parameter.AddPersonAndImage_Parameter;
import Face.Person.Result.AddPersonAndImage_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.config.Global;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.common.utils.file.FileUtils;
import com.snk.door.api.ControlService;
//import com.snk.door.api.finger.yz.FingerReaderYzService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.domain.Proof;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceAuthService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传用户与识别信息
 */
@Component
public class AddPersonImageService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(AddPersonImageService.class);

    @Autowired
    private IDeviceAuthService deviceAuthService;
    /*@Autowired
    private FingerReaderYzService fingerReaderYzService;*/

    public AddPersonImageService() {
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
        for (Auth item : opList) {
            Person person = new Person();
            IdentificationData[] data = null;
            try {
                if (DeviceType.FACE.getValue().equals(device.getDeviceType())) {    // 人脸
                    if (StringUtils.isNotEmpty(item.getPhoto())) {
                        data = new IdentificationData[1];
                        byte[] imgData = FileUtils.readImage(Global.getProfile() + StringUtils.substringAfter(item.getPhoto(), Constants.RESOURCE_PREFIX));
                        data[0] = new IdentificationData(1, 1, imgData);
                        person.IsFaceFeatureCode = true;
                        item.setFinger(null);
                    } else {
                        continue;
                    }
                } else if (DeviceType.FINGER.getValue().equals(device.getDeviceType())) {   // 指纹
                    /*List proofList = JSON.parseObject(item.getFinger(), ArrayList.class);
                    if (!CollectionUtils.isEmpty(proofList)) {
                        data = new IdentificationData[proofList.size()];
                        for (int i = 0; i < proofList.size(); i++) {
                            Proof proof = JSON.parseObject(JSON.toJSONString(proofList.get(i)), Proof.class);
                            byte[] imgData = null;
                            String base64 = fingerReaderYzService.fpConvert(proof.getValue(), device.getVerFinger());
                            if (StringUtils.isNotEmpty(base64)) {
                                imgData = fingerReaderYzService.Base64StringFormBytes(base64);
                            }
                            data[i] = new IdentificationData(2, Integer.valueOf(proof.getFinger()), imgData);
                        }
                        item.setPhoto(null);
                    } else {*/
                        continue;
                    //}
                }
                person.PName = item.getUserName();
                person.UserCode = item.getUserId().intValue();
                person.PCode =  item.getUserNo();
                if (StringUtils.isNotEmpty(item.getCardNo())) {
                    person.CardData = Long.valueOf(item.getCardNo());
                }
                person.OpenTimes = item.getOpenNum();
                person.CardType = "2".equals(item.getSpecial()) ? 1 : 0;
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
                if ("1".equals(item.getAdmin())) {   // 管理员
                    person.Identity = 1;
                }
                item.setStatus("1");
                item.setRemark(null);
            } catch (Exception e) {
                item.setRemark("上传失败：" + e.getMessage());
            }
            // 定义命令参数
            CommandDetail commandDetail = getCommandDetail(device);
            DoorRsp doorRsp = new DoorRsp(device.getId(), item);
            doorRsp.setSn(device.getSn());
            doorRsp.setSendMsg(sendMsg);
            commandDetail.UserPar = doorRsp;
            AddPersonAndImage_Parameter parameter = new AddPersonAndImage_Parameter(commandDetail, person, data);
            AddPersonAndImage cmd = new AddPersonAndImage(parameter);
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
            AddPersonAndImage_Result result = (AddPersonAndImage_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            Auth auth = (Auth) doorRsp.getParam();
            if (!result.UserUploadStatus) {
                auth.setRemark("上传失败");
            }
            deviceAuthService.insertAuth(auth);
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
