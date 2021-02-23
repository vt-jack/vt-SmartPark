/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.snk.door.demo;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.E_WeekDay;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import Door.Access.Door8800.Command.TimeGroup.AddTimeGroup;
import Door.Access.Door8800.Command.TimeGroup.Parameter.AddTimeGroup_Parameter;
import Door.Access.Door8800.Door8800Identity;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author F
 */
public class TimeGroup implements INConnectorEvent {

    private ConnectorAllocator _Allocator;

    public TimeGroup(ConnectorAllocator global) {
        if (global != null) {
            _Allocator = global;
        } else {
            System.out.println("命令对象不能为空");
            return;
        }
        _Allocator.AddListener(this);
    }

    public void SetTimeGroup() {

        CommandDetail commandDetail = new CommandDetail();
        commandDetail.Timeout = 8000;//此函数超时时间设定长一些
        TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.1.65", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
        commandDetail.Identity = new Door8800Identity("FC-8940H48120001", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        ArrayList<WeekTimeGroup> List = new ArrayList<WeekTimeGroup>();

        WeekTimeGroup wt = new WeekTimeGroup(8, 1);
        wt.GetItem(E_WeekDay.Monday).GetItem(0).SetBeginTime(10, 20);//星期一的第一个时段第一个开始时间10:20
        wt.GetItem(E_WeekDay.Monday).GetItem(0).SetEndTime(18, 20);//星期一的第一个时段第一个结束时间18:20
        wt.GetItem(E_WeekDay.Monday).GetItem(1).SetBeginTime(10, 20);//星期一的第一个时段第二个开始时间10:20
        wt.GetItem(E_WeekDay.Monday).GetItem(1).SetEndTime(18, 20);//星期一 的第一个时段第二个结束时间18:20
        List.add(wt);

        AddTimeGroup_Parameter parameter = new AddTimeGroup_Parameter(commandDetail, List);
        AddTimeGroup group;
        group = new AddTimeGroup(parameter);
        _Allocator.AddCommand(group);
    }

    @Override
    public void CommandCompleteEvent(INCommand inc, INCommandResult incr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void CommandProcessEvent(INCommand inc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ConnectorErrorEvent(INCommand inc, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail cd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void CommandTimeout(INCommand inc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PasswordErrorEvent(INCommand inc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ChecksumErrorEvent(INCommand inc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void WatchEvent(ConnectorDetail cd, INData indata) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ClientOnline(TCPServerClientDetail tcpscd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ClientOffline(TCPServerClientDetail tcpscd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
