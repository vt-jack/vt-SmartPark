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
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Command.CommandParameter;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Door8800.Command.Data.CardTransaction;
import Door.Access.Door8800.Command.System.*;
/**
 *
 * @author F
 */
public class DataMonitor implements INConnectorEvent {
     private ConnectorAllocator _Allocator;
    public DataMonitor(ConnectorAllocator global){
      if (global != null) {
            _Allocator = global;
        } else {
            System.out.println("命令对象不能为空");
            return;
        }
        //添加监听
        _Allocator.AddListener(this);
    }
    public void OpenTcpSetverMonitor(){
        CommandDetail commandDetail = new CommandDetail();
        TCPServerClientDetail tcpServerClientDetail=new  TCPServerClientDetail(8100);
        commandDetail.Connector=tcpServerClientDetail;
          CommandParameter par = new CommandParameter(commandDetail);//命令参数对象
    }
    public void OpenMonitor(){
        CommandDetail commandDetail = new CommandDetail();
        TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.2.151", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
        commandDetail.Identity = new Door8800Identity("SN-4024T20050002", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        if (commandDetail == null) {
            return;
        }

        CommandParameter par = new CommandParameter(commandDetail);//命令参数对象
        BeginWatch cmd = new BeginWatch(par);//命令对象

        _Allocator.OpenForciblyConnect(commandDetail.Connector);
        _Allocator.AddCommand(cmd);
    }
    
    public void CloseMonitor(){
          CommandDetail commandDetail = new CommandDetail();
         TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.1.65", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
           commandDetail.Identity = new Door8800Identity("AB-8940H48120001", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        if (commandDetail == null) {
            return;
        }
        CommandParameter par = new CommandParameter(commandDetail);//命令参数对象
        CloseWatch cmd = new CloseWatch(par);//命令对象

        _Allocator.CloseForciblyConnect(commandDetail.Connector);
        _Allocator.AddCommand(cmd);
    }
    
    //数据监控
    @Override
    public void WatchEvent(ConnectorDetail detial, INData event) {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("数据监控:");
            if (event instanceof Door8800WatchTransaction) {
                Door8800WatchTransaction WatchTransaction = (Door8800WatchTransaction) event;
                strBuf.append("，SN：");
                strBuf.append(WatchTransaction.SN);
                strBuf.append("\n");
             CardTransaction cardTransaction=(CardTransaction)WatchTransaction.EventData ;
             cardTransaction.IsEnter();
            } else {
                strBuf.append("，未知事件：");
                strBuf.append(event.getClass().getName());
            }
            System.out.println(strBuf.toString());
    }
    
      @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("<html>");
            strBuf.append("当前命令：");
            strBuf.append("<br/>正在处理： ");
            strBuf.append(cmd.getProcessStep());
            strBuf.append(" / ");
            strBuf.append(cmd.getProcessMax());
            strBuf.append("</html>");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.CommandProcessEvent() -- 发生错误：" + e.toString());
        }

    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            if (isStop) {
                strBuf.append("命令已手动停止!");
            } else {
                strBuf.append("网络连接失败!");
            }
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.ConnectorErrorEvent() --- " + e.toString());
        }

    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detial) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("网络通道故障，IP信息：");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.ConnectorErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        try {
            if (cmd instanceof SearchEquptOnNetNum) {
                return;
            }
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("命令超时，已失败！");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.CommandTimeout() -- " + e.toString());
        }

    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("通讯密码错误，已失败！");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.PasswordErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("命令返回的校验和错误，已失败！");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.ChecksumErrorEvent() -- " + e.toString());
        }

    }

    
    
    @Override
    public void ClientOnline(TCPServerClientDetail client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ClientOffline(TCPServerClientDetail client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
