package com.snk.door.api.face;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Face.AdditionalData.Parameter.ReadFile_Parameter;
import Face.AdditionalData.ReadFile;
import Face.AdditionalData.Result.ReadFile_Result;
import com.snk.common.config.Global;
import com.snk.common.constant.Constants;
import com.snk.common.exception.BusinessException;
import com.snk.common.utils.file.FileUploadUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.domain.User;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceAuthService;
import com.snk.door.service.IUserService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;

/**
 * 读取人员头像
 */
@Component
public class ReadUserPhotoService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadUserPhotoService.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IDeviceAuthService deviceAuthService;

    public ReadUserPhotoService() {
        super(DoorOperation.READ_FILE);
    }

    /**
     * 读取人员头像
     * @param device
     */
    public void readUserPhoto(Device device, List<Integer> userIdList) {
        Integer type = DeviceType.FACE.getValue().equals(device.getDeviceType()) ? 1 : 2;   // 文件类型 1 - 人员头像 2 - 指纹 3 - 记录照片
        userIdList.stream().forEach(item -> {
            // 定义命令参数
            CommandDetail commandDetail = getCommandDetail(device);
            DoorRsp doorRsp = new DoorRsp(device.getId(), item);
            doorRsp.setSn(device.getSn());
            commandDetail.UserPar = doorRsp;
            ReadFile_Parameter par = new ReadFile_Parameter(commandDetail, item, type, 1);
            ReadFile cmd = new ReadFile(par);
            // 添加命令到队列
            _Allocator.AddCommand(cmd);
        });
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
            Long userId = (Long) doorRsp.getParam();
            User user = userService.selectUserById(userId);
            String base64 = "data:image/png;base64," + Base64.encodeBase64String(result.FileDatas);
            String photo = uploadImage(base64);
            user.setPhoto(photo);
            userService.userUpdate(user);
            Auth queryParam = new Auth();
            queryParam.setDeviceId(doorRsp.getId());
            queryParam.setUserId(userId);
            Auth auth = deviceAuthService.selectAuth(queryParam);
            if (!ObjectUtils.isEmpty(auth)) {
                auth.setPhoto(photo);
                deviceAuthService.insertAuth(auth);
            }
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

    /**
     * 上传照片
     * @param base64
     * @return
     */
    private String uploadImage(String base64) {
        try{
            return FileUploadUtils.uploadBase64(Global.getUserPath(), base64);
        } catch (IOException e) {
            throw new BusinessException("上传照片出错！");
        }
    }

}
