/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.snk.door.demo;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.System.ReadSN;
import Door.Access.Door8800.Command.System.Result.ReadSN_Result;
import Door.Access.Door8800.Door8800Identity;

/**
 *
 * @author F
 */
public class ReadSn implements INConnectorEvent{
 private ConnectorAllocator _Allocator;
 public ReadSn(ConnectorAllocator global){
     if (global != null) {
            _Allocator = global;
        }else {
            System.out.println("命令对象不能为空");
            return;
        }
     _Allocator.AddListener(this);
 }
 public void GetSn(){
      CommandDetail commandDetail = new CommandDetail();
        commandDetail.Timeout=8000;//此函数超时时间设定长一些
        TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.2.198",8000);//IP  ， 端口(默认8000)
     //commandDetail.Event = new OpenDoor(_Allocator);
        commandDetail.Connector = tcpClientDetail;
        commandDetail.Identity = new Door8800Identity("SN-2024T28017060","FFFFFFFF",E_ControllerType.Door8800);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        CommandParameter par=new   CommandParameter(commandDetail);
        ReadSN cmd=new  ReadSN(par);
        _Allocator.AddCommand(cmd);
 }
    @Override
    public void CommandCompleteEvent(INCommand inc, INCommandResult incr) {
      ReadSN_Result sn= (ReadSN_Result) incr;
        System.out.println("Demo.ReadSn.CommandCompleteEvent()"+sn.SN);
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
