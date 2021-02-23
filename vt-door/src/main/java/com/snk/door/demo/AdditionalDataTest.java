package com.snk.door.demo;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Connector.UDP.UDPDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Door8800Identity;
import Face.AdditionalData.Parameter.ReadFile_Parameter;
import Face.AdditionalData.ReadFile;

import java.util.concurrent.Semaphore;

/**
 * 人员附加数据测试
 */
public class AdditionalDataTest implements INConnectorEvent {
    String LocalIp = "192.168.1.140";
    int LocalPort = 8801;
    ConnectorAllocator _Allocator;
    private final Semaphore available = new Semaphore(0, true);
    ///等待

    public void syn() {
        try {
            available.acquire();
        } catch (Exception e) {
        }

    }
//关闭等待

    public void release() {
        _Allocator.Release();
        available.release();
    }

    private CommandDetail getDetail() {
        CommandDetail commandDetail = new CommandDetail();
        /**
         * 此函数超时时间设定长一些
         */
        commandDetail.Timeout = 8000;
        UDPDetail cDetail = new UDPDetail("192.168.1.71", 996, LocalIp, LocalPort);
        commandDetail.Connector = cDetail;
        /**
         * 设置SN(16位字符)，密码(8位十六进制字符)，设备类型
         */
        Door8800Identity idt = new Door8800Identity("FC-8300T20044264", "FFFFFFFF", E_ControllerType.Door8900);
        commandDetail.Identity = idt;
        return commandDetail;
    }

    public AdditionalDataTest() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _Allocator.UDPBind(LocalIp, LocalPort);

    }

    public void readFile() {
        ReadFile_Parameter parameter = new ReadFile_Parameter(getDetail(), 73407, 3, 1);
        ReadFile cmd = new ReadFile(parameter);
        _Allocator.AddCommand(cmd);
    }
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {

    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {

    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {

    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {

    }

    @Override
    public void CommandTimeout(INCommand cmd) {

    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {

    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {

    }

    @Override
    public void WatchEvent(ConnectorDetail detail, INData event) {

    }

    @Override
    public void ClientOnline(TCPServerClientDetail client) {

    }

    @Override
    public void ClientOffline(TCPServerClientDetail client) {

    }
}
