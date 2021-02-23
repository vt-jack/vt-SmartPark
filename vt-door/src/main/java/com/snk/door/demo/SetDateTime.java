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
import Door.Access.Connector.*;
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.DateTime.Parameter.WriteTimeDefine_Parameter;
import Door.Access.Door8800.Command.DateTime.ReadTime;
import Door.Access.Door8800.Command.DateTime.WriteTime;
import Door.Access.Door8800.Command.DateTime.WriteTimeDefine;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import java.util.Calendar;
/**
 *
 * @author F
 */
public class SetDateTime  implements INConnectorEvent{
        private ConnectorAllocator _Allocator;

    public SetDateTime(ConnectorAllocator global) {
        if (global != null) {
            _Allocator = global;
        } else {
            System.out.println("命令对象不能为空");
        }
        _Allocator.AddListener(this);
    }

    public void Set() {
        //创建连接信息对象
        CommandDetail commandDetail = new CommandDetail();
        TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.1.65", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
           commandDetail.Identity = new Door8800Identity("AB-8940H48120001", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        commandDetail.Timeout = 5000;//设置超时
        Calendar c = Calendar.getInstance();
        WriteTimeDefine_Parameter par = new WriteTimeDefine_Parameter(commandDetail, c);
        WriteTime cmd = new WriteTime(par);
        //添加命令到队列
        _Allocator.AddCommand(cmd);
    }

    public void SetCustomize() {
        // TODO add your handling code here:
        CommandDetail commandDetail = new CommandDetail();
         TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.1.65", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
           commandDetail.Identity = new Door8800Identity("AB-8940H48120001", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        commandDetail.Timeout = 5000;
        Calendar c = Calendar.getInstance();
        //设置时间
        c.set(2018, 1, 28, 8, 8, 8);
        WriteTimeDefine_Parameter par = new WriteTimeDefine_Parameter(commandDetail, c);//参数化时间对象参数
        WriteTimeDefine cmd = new WriteTimeDefine(par);
        //添加命令到队列
        _Allocator.AddCommand(cmd);
    }

    public void Get() {
        CommandDetail commandDetail = new CommandDetail();
         TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.1.65", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
           commandDetail.Identity = new Door8800Identity("AB-8940H48120001", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        commandDetail.Timeout = 5000;
        CommandParameter par = new CommandParameter(commandDetail);
        ReadTime cmd = new ReadTime(par);//初始化读取时间命令对象
        //添加命令到队列
        _Allocator.AddCommand(cmd);
    }
    
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        if (cmd.getClass().getName().equals("Door.Access.Door8800.Command.DateTime.ReadTime")) {
            //返回时间数据
            System.out.println(result.toString());
        }
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
    public void WatchEvent(ConnectorDetail detial, INData event) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("数据监控:");
            if (event instanceof Door8800WatchTransaction) {
                Door8800WatchTransaction WatchTransaction = (Door8800WatchTransaction) event;
                strBuf.append("，SN：");
                strBuf.append(WatchTransaction.SN);
                strBuf.append("\n");
            } else {
                strBuf.append("，未知事件：");
                strBuf.append(event.getClass().getName());
            }
            strBuf = null;

        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.WatchEvent() -- " + e.toString());
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
