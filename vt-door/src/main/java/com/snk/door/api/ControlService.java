package com.snk.door.api;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Connector.UDP.UDPDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import com.alibaba.fastjson.JSON;
import com.snk.common.config.SocketIOConfig;
import com.snk.common.constant.Constants;
import com.snk.common.utils.CacheUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.enums.ModelType;
import com.snk.door.socketio.ISocketIOService;
import com.snk.door.socketio.entity.Message;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

/**
 * 控制板父类
 */
@Data
public class ControlService implements INConnectorEvent {

    private static final Logger log = LoggerFactory.getLogger(ControlService.class);

    @Value("${door.bindPort}")
    private Integer bindPort;

    protected ConnectorAllocator _Allocator;

    private DoorOperation operation;

    @Autowired
    private ISocketIOService socketIOService;
    @Autowired
    private SocketIOConfig socketIOConfig;

    public ControlService(DoorOperation operation) {
        this.operation = operation;
        _Allocator = ConnectorAllocator.GetAllocator();
    }

    /**
     * 组装控制器连接信息
     */
    public CommandDetail getCommandDetail(Device device) {
        log.info("设备SN：{}，设备名称：{}，设备类型：{}", device.getSn(), device.getName(), DeviceType.getDeviceType(device.getDeviceType()));
        //定义控制器连接信息
        CommandDetail commandDetail = new CommandDetail();
        commandDetail.Connector = (TCPServerClientDetail) CacheUtils.get(CacheUtils.DOOR_CONNECT_CACHE, device.getSn());
        if (ObjectUtils.isEmpty(commandDetail.Connector)) {
            if (isControl(device)) {    // 控制板
                commandDetail.Connector = new TCPClientDetail(device.getIp(), device.getTcpPort());
            } else {    // 人脸|指纹机
                commandDetail.Connector = new UDPDetail(device.getIp(), device.getUdpPort(), device.getServerIp(), bindPort);
            }
        }
        log.info("commandDetail.Connector：{}", JSON.toJSONString(commandDetail.Connector));
        //设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        commandDetail.Identity = new Door8800Identity(StringUtils.isEmpty(device.getSn()) ? Constants.DEFAULT_SN : device.getSn(),
                StringUtils.isEmpty(device.getPwd()) ? Constants.DEFAULT_PWD : device.getPwd(), ModelType.getControllerType(device.getModelType()));
        commandDetail.Event = this;
        commandDetail.Timeout = 8000;
        return commandDetail;
    }

    /**
     * 是否是控制板
     * @param device
     * @return
     */
    public boolean isControl(Device device) {
        if (DeviceType.CONTROL.getValue().equals(device.getDeviceType())) { // 控制板
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取发送消息对象
     * @param doorRsp
     * @return
     */
    public Message getMessage(DoorRsp doorRsp) {
        Message message = new Message(doorRsp.getUserId());
        message.setContent(JSON.toJSONString(doorRsp));
        return message;
    }

    /**
     * 校验参数是否为空
     * @param device 设备信息
     */
    public boolean paramIsEmpty(Device device) {
        if (ObjectUtils.isEmpty(device.getParam())) {
            if (!StringUtils.isEmpty(getOperation().getEvent())) {
                DoorRsp doorRsp = new DoorRsp(device.getId());
                doorRsp.setResult(Constants.DEFAULT_ERROR_MSG);
                pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            }
            log.error("参数不能为空：{},SN：{}", operation, device.getSn());
            return true;
        }
        return false;
    }

    /**
     * 推送消息到前台页面
     *
     * @param event 事件
     */
    public void pushMessage(String event, Message message) {
        socketIOService.pushMessage(event, message);
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
            if (!StringUtils.isEmpty(getOperation().getEvent())) {
                DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
                doorRsp.setResult(operation.getResult());   // 操作结果
                // 发送消息
                pushMessage(Message.OP_DEVICE, getMessage(doorRsp));
            }
            log.info("命令完成：{}", operation.getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

    /**
     * 命令进程
     *
     * @param cmd
     */
    @Override
    public void CommandProcessEvent(INCommand cmd) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("当前命令：");
        sb.append("<br/>正在处理： ");
        sb.append(cmd.getProcessStep());
        sb.append(" / ");
        sb.append(cmd.getProcessMax());
        sb.append("</html>");
        log.info(sb.toString());
    }

    /**
     * 发生错误时调用
     * @param cmd
     */
    public void error(INCommand cmd, String errorMsg) {
        try {
            String msg = StringUtils.isEmpty(errorMsg) ? operation.getErrorMsg() : errorMsg;
            if (!StringUtils.isEmpty(getOperation().getEvent())) {
                DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
                doorRsp.setResult(msg);  // 操作结果
                // 发送消息
                Message message = getMessage(doorRsp);
                if (DoorOperation.READ_TRANSACTION_DATABASE.equals(getOperation())) {
                    message.setCode(Constants.DEFAULT_ERROR_CODE);
                }
                pushMessage(getOperation().getEvent(), message);
            }
            log.error("发生错误：{}, 命令：{}, 请求参数：{}", msg, getOperation().getDescribe(), JSON.toJSONString(cmd.getCommandParameter().getCommandDetail().Connector));
        } catch (Exception e) {
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

    /**
     * 连接通道发生错误时回调
     *
     * @param cmd
     * @param isStop 是否是用户停止时发生的？
     */
    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        StringBuffer sb = new StringBuffer();
        if (isStop) {
            sb.append("命令已手动停止!");
        } else {
            sb.append("网络连接失败!");
        }
        error(cmd, "网络连接失败");
    }

    /**
     * 连接通道发生错误时回调
     *
     * @param detail
     */
    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {
        log.error("网络通道故障!");
    }

    /**
     * 命令超时时，触发此回到函数
     *
     * @param cmd 此命令的内容
     */
    @Override
    public void CommandTimeout(INCommand cmd) {
        if (cmd instanceof SearchEquptOnNetNum) {
            return;
        }
        error(cmd, "命令超时");
    }

    /**
     * 通讯密码错误
     *
     * @param cmd
     */
    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        error(cmd, "通讯密码错误");
    }

    /**
     * 通讯校验和错误
     *
     * @param cmd
     */
    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        error(cmd, "命令返回的校验和错误");
    }

    /**
     * 监控数据响应
     */
    @Override
    public void WatchEvent(ConnectorDetail detial, INData event) { }

    /**
     * 客户端上线
     */
    @Override
    public void ClientOnline(TCPServerClientDetail client) {
        log.info("客户端已上线!");
    }

    /**
     * 客户端离线
     */
    @Override
    public void ClientOffline(TCPServerClientDetail client) {
        log.info("客户端已离线!");
    }
}
