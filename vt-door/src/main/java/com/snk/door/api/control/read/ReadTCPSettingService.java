package com.snk.door.api.control.read;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.System.ReadTCPSetting;
import Door.Access.Door8800.Command.System.Result.ReadTCPSetting_Result;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 读取控制器TCP网络参数
 */
@Component
public class ReadTCPSettingService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(ReadTCPSettingService.class);

    @Autowired
    private IDeviceService deviceService;

    public ReadTCPSettingService() {
        super(DoorOperation.READ_TCP_SETTING);
    }

    /**
     * 读取控制器TCP网络参数
     * @param device 设备信息
     */
    public void readTCPSetting(Device device) {
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId());
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadTCPSetting cmd = new ReadTCPSetting(par);
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
            ReadTCPSetting_Result result = (ReadTCPSetting_Result) incr;
            DoorRsp doorRsp = (DoorRsp) cmd.getCommandParameter().getCommandDetail().UserPar;
            // 更新TCP网络参数
            Device device = new Device();
            device.setSn(doorRsp.getSn());
            device.setAutoIp(result.TCP.GetAutoIP());
            device.setIp(result.TCP.GetIP());
            device.setMac(result.TCP.GetMAC());
            device.setIpMask(result.TCP.GetIPMask());
            device.setIpGateway(result.TCP.GetIPGateway());
            device.setDns(result.TCP.GetDNS());
            device.setDnsBak(result.TCP.GetDNSBackup());
            device.setTcpPort(result.TCP.GetTCPPort());
            device.setUdpPort(result.TCP.GetUDPPort());
            device.setServerIp(result.TCP.GetServerIP());
            device.setServerPort(result.TCP.GetServerPort());
            device.setServerAddress(result.TCP.GetServerAddr());
            deviceService.updateDeviceBySn(device);
            log.info("命令完成：{},autoIp：{},ip:{},mac:{},ipMask:{},ipGateway:{},dns:{},dnsBak:{},tcpPort:{},udpPort:{},serverIp:{},serverPort:{},serverAddr:{}",
                    getOperation().getResult(), result.TCP.GetAutoIP(), result.TCP.GetIP(), result.TCP.GetMAC(), result.TCP.GetIPMask(), result.TCP.GetIPGateway(), result.TCP.GetDNS(),
                    result.TCP.GetDNSBackup(), result.TCP.GetTCPPort(), result.TCP.GetUDPPort(), result.TCP.GetServerIP(), result.TCP.GetServerPort(), result.TCP.GetServerAddr());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }

}
