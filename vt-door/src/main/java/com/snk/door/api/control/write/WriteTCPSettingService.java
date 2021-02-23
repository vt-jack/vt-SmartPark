package com.snk.door.api.control.write;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TCPDetail;
import Door.Access.Door8800.Command.System.Parameter.WriteTCPSetting_Parameter;
import Door.Access.Door8800.Command.System.WriteTCPSetting;
import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.utils.DateUtils;
import com.snk.door.api.ControlService;
import com.snk.door.api.rsp.DoorRsp;
import com.snk.door.domain.Device;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 修改控制器TCP网络参数
 */
@Component
public class WriteTCPSettingService extends ControlService {

    private static final Logger log = LoggerFactory.getLogger(WriteTCPSettingService.class);

    @Autowired
    private IDeviceService deviceService;

    public WriteTCPSettingService() {
        super(DoorOperation.WRITE_TCP_SETTING);
    }

    /**
     * 修改控制器TCP网络参数
     * @param device
     */
    public void writeTCPSetting(Device device) {
        if (paramIsEmpty(device)) {
            return;
        }
        Map<String, String> map = (Map) device.getParam();
        TCPDetail tcp = new TCPDetail();
        tcp.SetMAC(map.get("mac"));
        tcp.SetIP(map.get("ip"));
        tcp.SetIPMask(map.get("ipMask"));
        tcp.SetIPGateway(map.get("ipGateway"));
        tcp.SetTCPPort(Integer.valueOf(map.get("tcpPort")));
        tcp.SetUDPPort(Integer.valueOf(map.get("udpPort")));
        tcp.SetServerIP(map.get("serverIp"));
        tcp.SetServerPort(Integer.valueOf(map.get("serverPort")));
        tcp.SetServerAddr(map.get("serverAddress"));
        tcp.SetDNS(map.get("dns"));
        tcp.SetDNSBackup(map.get("dnsBak"));
        // 定义命令参数
        CommandDetail commandDetail = getCommandDetail(device);
        DoorRsp doorRsp = new DoorRsp(device.getId(), tcp);
        doorRsp.setSn(device.getSn());
        commandDetail.UserPar = doorRsp;
        WriteTCPSetting_Parameter par = new WriteTCPSetting_Parameter(commandDetail, tcp);
        WriteTCPSetting cmd = new WriteTCPSetting(par);
        // 添加命令到队列
        _Allocator.AddCommand(cmd);
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
            doorRsp.setResult(getOperation().getResult());
            // 更新TCP网络参数
            Device device = new Device();
            TCPDetail tcp = (TCPDetail) doorRsp.getParam();
            device.setSn(doorRsp.getSn());
            device.setIp(tcp.GetIP());
            device.setIpMask(tcp.GetIPMask());
            device.setIpGateway(tcp.GetIPGateway());
            device.setTcpPort(tcp.GetTCPPort());
            device.setUdpPort(tcp.GetUDPPort());
            device.setServerIp(tcp.GetServerIP());
            device.setServerPort(tcp.GetServerPort());
            device.setServerAddress(tcp.GetServerAddr());
            device.setDns(tcp.GetDNS());
            device.setDnsBak(tcp.GetDNSBackup());
            device.setUpdateBy(doorRsp.getUserName());
            device.setUpdateTime(DateUtils.getNowDate());
            deviceService.updateDeviceBySn(device);
            pushMessage(getOperation().getEvent(), getMessage(doorRsp));
            log.info("命令完成：{}", getOperation().getResult());
        } catch (Exception e) {
            error(cmd, Constants.DEFAULT_ERROR_MSG);
            log.error("发生系统错误：{}", e.getMessage());
            return;
        }
    }
}
