package com.snk.door.demo;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
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
import Face.Door.CloseDoor;
import Face.Door.HoldDoor;

import java.util.concurrent.Semaphore;

/**
 * 门方法测试
 */
public class DoorTest implements INConnectorEvent {

    String LocalIp = "192.168.2.32";
    int LocalPort = 8100;
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
        UDPDetail cDetail = new UDPDetail("192.168.2.89", 8101, LocalIp, LocalPort);
        commandDetail.Connector = cDetail;
        /**
         * 设置SN(16位字符)，密码(8位十六进制字符)，设备类型
         */
        Door8800Identity idt = new Door8800Identity("FC-8300T20064239", "FFFFFFFF", E_ControllerType.Door8900);
        commandDetail.Identity = idt;
        return commandDetail;
    }

    public DoorTest() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _Allocator.UDPBind(LocalIp, LocalPort);

    }

    /**
     * 远程关门
     */
    public void CloseDoor() {
        CommandParameter par = new CommandParameter(getDetail());
        CloseDoor cmd = new CloseDoor(par);
        _Allocator.AddCommand(cmd);
    }

    /**
     * 门常开
     */
    public void HoldDoor() {
        CommandParameter par = new CommandParameter(getDetail());
        HoldDoor cmd = new HoldDoor(par);
        _Allocator.AddCommand(cmd);
    }

    /**
     * 命令执行成功返回
     *
     * @param cmd    此次事件所关联的命令详情
     * @param result 命令包含的结果
     */
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        System.out.println("命令执行成功" + result.toString());
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        int processMax = cmd.getProcessMax();
        int processStep = cmd.getProcessStep();
        System.out.println("当前执行进度：" + processMax + "/" + processStep);
    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        System.out.println("命令错误");
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {
        System.out.println("连接错误");
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        System.out.println("连接超时");
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        System.out.println("通讯密码错误");
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
