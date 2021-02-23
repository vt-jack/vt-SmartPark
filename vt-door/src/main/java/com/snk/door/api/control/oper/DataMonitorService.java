package com.snk.door.api.control.oper;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.BytesData;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.*;
import Door.Access.Door8800.Command.System.BeginWatch;
import Door.Access.Door8800.Command.System.ReadWatchState;
import Door.Access.Door8800.Command.System.Result.ReadWatchState_Result;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Door8800.Packet.Door8800Decompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Packet.INPacketModel;
import Door.Access.Packet.PacketDecompileAllocator;
import Face.Data.FaceCommandWatchResponse;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.SymbolConstants;
import com.snk.common.utils.CacheUtils;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.face.ReadWatchFileService;
import com.snk.door.domain.Device;
import com.snk.door.domain.Model;
import com.snk.door.domain.User;
import com.snk.door.domain.VisitReg;
import com.snk.door.enums.*;
import com.snk.door.mongo.entity.CurrentRecord;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.IModelService;
import com.snk.door.service.IUserService;
import com.snk.door.service.IVisitRegService;
import com.snk.door.socketio.ISocketIOService;
import com.snk.door.socketio.entity.Message;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据监控
 */
@Component
public class DataMonitorService implements INConnectorEvent {

    private static final Logger log = LoggerFactory.getLogger(DataMonitorService.class);

    protected ConnectorAllocator _Allocator;

    private DoorOperation operation = DoorOperation.BEGIN_WATCH;

    @Autowired
    private ISocketIOService socketIOService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IModelService modelService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ReadWatchFileService readWatchFileService;
    @Autowired
    private IVisitRegService visitRegService;

    public DataMonitorService(@Value("${socketio.host}") String host, @Value("${door.serverPort}") Integer serverPort, @Value("${door.bindPort}") Integer bindPort) {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _Allocator.Listen(host, serverPort);
        _Allocator.UDPBind(host, bindPort);
        log.info("TCP监听完成：{}:{}", host, serverPort);
    }

    /**
     * 当命令完成时，会触发此函数回调
     *
     * @param cmd 此次事件所关联的命令详情
     * @param incr 命令包含的结果
     */
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult incr) {
        if (incr instanceof ReadWatchState_Result) {
            String sn = ((Door8800Identity) cmd.getCommandParameter().getCommandDetail().Identity).GetSN();
            ReadWatchState_Result result = (ReadWatchState_Result) incr;
            log.info("{}监控状态：{}", sn, 0 == result.State ? "关闭" : "开启");
            if (0 == result.State) {    // 未开启
                // 开启数据监控
                BeginWatch cmd1 = new BeginWatch((CommandParameter) cmd.getCommandParameter());
                // 添加命令到队列
                _Allocator.AddCommand(cmd1);
                log.info("{}监控开启命令已发送", sn);
            }
        }
    }

    /**
     * 命令进程
     *
     * @param cmd
     */
    @Override
    public void CommandProcessEvent(INCommand cmd) {

    }

    /**
     * 发生错误时调用
     * @param cmd
     */
    public void error(INCommand cmd, String errorMsg) {
        log.error("发生错误：{}, 请求参数：{}", StringUtils.isEmpty(errorMsg) ? operation.getResult() : errorMsg,
                JSON.toJSONString(cmd.getCommandParameter().getCommandDetail().Connector));
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
    public void WatchEvent(ConnectorDetail detail, INData event) {
        if (event instanceof BytesData) {
            authentication(detail, event);
        }else {
            transaction(detail, event);
        }
    }

    /**
     * 身份验证
     * @param detail
     * @param event
     */
    private void authentication(ConnectorDetail detail, INData event) {
        BytesData b = (BytesData) event;
        ByteBuf dBuf = b.GetBytes();
        if (dBuf.getByte(0) == 0x7e) {
            TCPServerClientDetail cd = (TCPServerClientDetail) detail;
            Door8800Decompile decompile = new Door8800Decompile();
            ArrayList<INPacketModel> oRetPack = new ArrayList<>(10);
            if (decompile.Decompile(dBuf, oRetPack)) {
                Door8800PacketModel m = (Door8800PacketModel) oRetPack.get(0);
                // m.GetSN() -- 这个就是此控制器的SN号，
                String sn = m.GetSN();
                // 保存连接信息到缓存，后面所有连接信息都用这个
                CacheUtils.put(CacheUtils.DOOR_CONNECT_CACHE, cd.ClientID + "", sn);
                CacheUtils.put(CacheUtils.DOOR_CONNECT_CACHE, sn, cd);
                // 获取型号配置信息
                Model model = modelService.selectModelByModel(sn.substring(0, 8));
                if (!ObjectUtils.isEmpty(model)) {
                    //定义控制器连接信息
                    CommandDetail commandDetail = new CommandDetail();
                    commandDetail.Connector = cd;   // 连接参数
                    //设置SN(16位字符)，密码(8位十六进制字符)，设备类型
                    commandDetail.Identity = new Door8800Identity(m.GetSN(), Long.toHexString(m.GetPassword()), ModelType.getControllerType(model.getDeviceType()));
                    Boolean isControl = DeviceType.CONTROL.getValue().equals(model.getDeviceType());
                    commandDetail.UserPar = isControl;
                    CommandParameter par = new CommandParameter(commandDetail);
                    if (isControl) {  // 控制板
                        if (ModelType.COMMON.getValue().equals(model.getType())) {    // 普通板
                            // 添加控制板解析器
                            _Allocator.AddWatchDecompile(cd, PacketDecompileAllocator.GetDecompile(E_ControllerType.Door8800));
                        } else {    // 高级板
                            // 添加控制板解析器
                            _Allocator.AddWatchDecompile(cd, PacketDecompileAllocator.GetDecompile(E_ControllerType.Door8900));
                        }
                        // 读取监控状态 看是否需要开启
                        ReadWatchState cmd = new ReadWatchState(par);
                        // 添加命令到队列
                        _Allocator.AddCommand(cmd);
                    } else {    // 人脸|指纹机
                        // 添加人脸解析器
                        _Allocator.AddWatchDecompile(cd, new FaceCommandWatchResponse());
                    }
                    // 更新工作状态为：在线
                    Device device = new Device();
                    device.setSn(sn);
                    device.setWorkState(WorkState.ON_LINE.getValue());
                    deviceService.updateDeviceBySn(device);
                }
            }
        }
    }

    /**
     * 监控消息处理
     * @param detail
     * @param event
     */
    private void transaction(ConnectorDetail detail, INData event){
        if (event instanceof Door8800WatchTransaction) {
            Door8800WatchTransaction watchTransaction = (Door8800WatchTransaction) event;
            String sn = watchTransaction.SN;    // SN
            if (watchTransaction.EventData instanceof DefinedTransaction) {
                return;
            }
            log.info("SN：{}，监控事件：{}", sn, watchTransaction.EventData.getClass());
            CurrentRecord record = new CurrentRecord();
            record.setSn(sn);
            record.setType(Integer.valueOf(watchTransaction.EventData.TransactionType()));
            record.setSerialNumber(watchTransaction.EventData.SerialNumber);
            Short controlPort = 1;   // 控制端口
            List<Device> deviceList = deviceService.selectDeviceBySn(sn);
            if (CollectionUtils.isEmpty(deviceList)) {
                return;
            }
            Boolean isFace = false, isFinger = false;
            Device device = deviceList.get(0);
            if (DeviceType.CONTROL.getValue().equals(device.getDeviceType())) { // 控制器
                switch (watchTransaction.EventData.TransactionType()) {
                    case 1: // 刷卡记录
                        String modelType = device.getModelType(); // 控制板类型
                        String cardNo;
                        Short transactionCode;
                        Boolean isEnter, isExit;
                        if (ModelType.COMMON.getValue().equals(modelType)) {    // 普通板
                            CardTransaction cardTransaction = (CardTransaction) watchTransaction.EventData;
                            controlPort = cardTransaction.DoorNum();
                            cardNo = cardTransaction.CardData;
                            transactionCode = cardTransaction.TransactionCode();
                            isEnter = cardTransaction.IsEnter();
                            isExit = cardTransaction.IsExit();
                        } else {    // 高级板
                            Door.Access.Door89H.Command.Data.CardTransaction cardTransaction = (Door.Access.Door89H.Command.Data.CardTransaction) watchTransaction.EventData;
                            controlPort = cardTransaction.DoorNum();
                            cardNo = cardTransaction.CardData;
                            transactionCode = cardTransaction.TransactionCode();
                            isEnter = cardTransaction.IsEnter();
                            isExit = cardTransaction.IsExit();
                        }
                        this.setUserInfo(cardNo, record);
                        record.setDescribe(CardRecord.getComment(transactionCode));   // 描述
                        record.setIoType(isEnter ? "1" : isExit ? "2" : null);
                        break;
                    case 2: // 出门按钮记录
                        ButtonTransaction buttonTransaction = (ButtonTransaction) watchTransaction.EventData;
                        controlPort = buttonTransaction.Door;
                        record.setDescribe(ButtonRecord.getComment(buttonTransaction.TransactionCode()));
                        break;
                    case 3: // 门磁记录
                        DoorSensorTransaction doorSensorTransaction = (DoorSensorTransaction) watchTransaction.EventData;
                        controlPort = doorSensorTransaction.Door;
                        record.setDescribe(DoorRecord.getComment(doorSensorTransaction.TransactionCode()));
                        break;
                    case 4: // 软件操作记录
                        SoftwareTransaction softwareTransaction = (SoftwareTransaction) watchTransaction.EventData;
                        controlPort = softwareTransaction.Door;
                        record.setDescribe(SoftwareRecord.getComment(softwareTransaction.TransactionCode()));
                        break;
                    case 5: // 报警记录
                        AlarmTransaction alarmTransaction = (AlarmTransaction) watchTransaction.EventData;
                        controlPort = alarmTransaction.Door;
                        record.setDescribe(AlarmRecord.getComment(alarmTransaction.TransactionCode()));
                        break;
                    case 6: // 系统记录
                        SystemTransaction systemTransaction = (SystemTransaction) watchTransaction.EventData;
                        record.setDescribe(SystemRecord.getComment(systemTransaction.TransactionCode()));
                        break;
                    default:
                        break;
                }
                record.setDeviceType(DeviceType.CONTROL.getValue());
            } else {
                switch (watchTransaction.EventData.TransactionType()) {
                    case 1: // 认证记录
                        Face.Data.CardTransaction card = (Face.Data.CardTransaction) watchTransaction.EventData;
                        this.setUserInfo(card.getUserCode(), record);
                        Short transactionCode = card.TransactionCode();
                        if (!CardRecordFace.isCard(transactionCode)) {
                            record.setCardNo(null);
                        }
                        if (CardRecordFace.isFace(transactionCode)) {
                            isFace = true;
                        }
                        if (CardRecordFace.isFinger(transactionCode)) {
                            isFinger = true;
                        }
                        record.setIoType(card.getAccessType() + "");
                        record.setDescribe(CardRecordFace.getComment(transactionCode));
                        if (isFace) {
                            readWatchFileService.readFile(device, card, 3);
                        }
                        if (isFinger) {
                            readWatchFileService.readFile(device, card, 2);
                        }
                        break;
                    case 2: // 门磁记录
                        Face.Data.DoorSensorTransaction door = (Face.Data.DoorSensorTransaction) watchTransaction.EventData;
                        record.setDescribe(DoorRecord.getComment(door.TransactionCode()));
                        break;
                    case 3: // 系统记录
                        Face.Data.SystemTransaction system = (Face.Data.SystemTransaction) watchTransaction.EventData;
                        record.setDescribe(SystemRecordFace.getComment(system.TransactionCode()));
                        break;
                    case 4: // 包活/连接测试记录
                        log.info("保活包消息.................");
                        return;
                    default:
                        return;
                }
            }
            if (!ObjectUtils.isEmpty(controlPort)) {
                // 查询设备信息
                for (Device item : deviceList) {
                    if (Integer.valueOf(controlPort).equals(item.getControlPort())) {
                        record.setDeviceId(item.getId());
                        record.setDeviceName(item.getName());
                        record.setPositionName(item.getPositionName());
                    }
                }
            }
            record.setCreateTime(DateUtils.getTime());
            Message message = new Message(null);
            message.setContent(JSON.toJSONString(record));
            socketIOService.pushMessage(operation.getEvent(), message);
        } else {
            log.warn("未知事件");
        }
    }

    /**
     * 补充用户信息
     * @param cardNo
     * @param record
     */
    private void setUserInfo(String cardNo, CurrentRecord record) {
        this.setUserInfo(record, userService.selectUserByCardNo(cardNo));
        // 查询登记访客信息
        VisitReg visitReg = visitRegService.selectVisitRegByCardNo(cardNo);
        if (!ObjectUtils.isEmpty(visitReg)) {
            this.setVisitTime(visitReg);
        }
    }

    /**
     * 补充用户信息
     * @param userId
     * @param record
     */
    private void setUserInfo(Long userId, CurrentRecord record) {
        this.setUserInfo(record, userService.selectUserById(userId));
        // 查询登记访客信息
        VisitReg visitReg = visitRegService.selectVisitRegById(userId);
        if (!ObjectUtils.isEmpty(visitReg)) {
            this.setVisitTime(visitReg);
        }
    }

    /**
     * 更新用户信息
     *
     * @param record
     * @param user
     */
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
     * 更新访客来访时间
     *
     * @param visitReg
     */
    private void setVisitTime(VisitReg visitReg) {
        if (StringUtils.isEmpty(visitReg.getVisitTime())) {
            visitReg.setVisitTime(DateUtils.getTimeByLocalDateTime(LocalDateTime.now()));
            visitReg.setUpdateBy("DataMonitor");
            visitRegService.updateVisitReg(visitReg);
        }
    }

    /**
     * 客户端上线
     */
    @Override
    public void ClientOnline(TCPServerClientDetail client) {
        log.info("有客户端上线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
    }

    /**
     * 客户端离线
     */
    @Override
    public void ClientOffline(TCPServerClientDetail client) {
        log.info("有客户端离线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
        Object object = CacheUtils.get(CacheUtils.DOOR_CONNECT_CACHE, client.ClientID + "");
        if (!ObjectUtils.isEmpty(object)) {
            // 删除缓存
            CacheUtils.remove(CacheUtils.DOOR_CONNECT_CACHE, object.toString());
            CacheUtils.remove(CacheUtils.DOOR_CONNECT_CACHE, client.ClientID + "");
            log.info("{}删除缓存完成", object);
            // 更新工作状态为：离线
            Device device = new Device();
            device.setSn(object.toString());
            device.setWorkState(WorkState.OFF_LINE.getValue());
            deviceService.updateDeviceBySn(device);
        }
    }
}
