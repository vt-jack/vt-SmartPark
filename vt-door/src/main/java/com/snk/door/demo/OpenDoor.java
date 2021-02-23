/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.snk.door.demo;
import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.*;
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.Door.Parameter.OpenDoor_Parameter;
import Door.Access.Door8800.Command.Door.Parameter.RemoteDoor_Parameter;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
/**
 *
 * @author F
 */
public class OpenDoor  implements INConnectorEvent{
    private ConnectorAllocator _Allocator;
    private boolean mIsClose;

    public OpenDoor(ConnectorAllocator global) {
        if (global != null) {
            _Allocator = global;
        } else {
            System.out.println("命令对象不能为空");
            return;
        }
        //添加监听
        //_Allocator.AddListener(this);
    }

    public void OpenDoor() {
        //定义控制器连接信息
        CommandDetail commandDetail = new CommandDetail();
        TCPClientDetail tcpClientDetail = new TCPClientDetail("127.0.0.1", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
        commandDetail.Identity = new Door8800Identity("SN-2024T28017061", "FFFFFFFF", E_ControllerType.Door8800);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        commandDetail.Event = this;
        //定义命令参数
        OpenDoor_Parameter openDoor_parameter = new OpenDoor_Parameter(commandDetail);//初始化开门参数
        openDoor_parameter.Door.SetDoor(1, 1);//设定1号门执行操作
        Door.Access.Door8800.Command.Door.OpenDoor openDoor = new Door.Access.Door8800.Command.Door.OpenDoor(openDoor_parameter);
        //添加命令到队列
        _Allocator.AddCommand(openDoor);
    }

    public void CloseDoor() {
        CommandDetail commandDetail = new CommandDetail();
         TCPClientDetail tcpClientDetail = new TCPClientDetail("127.0.0.1", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
           commandDetail.Identity = new Door8800Identity("AB-8940H48120001", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        //定义命令参数
        RemoteDoor_Parameter par = new RemoteDoor_Parameter(commandDetail);//初始化开门参数
        par.Door.SetDoor(1, 1);//设定1号门执行操作
        par.Door.SetDoor(2, 1);//设定2号门不执行操作
        Door.Access.Door8800.Command.Door.CloseDoor closeDoor = new Door.Access.Door8800.Command.Door.CloseDoor(par);
        //添加命令到队列
        _Allocator.AddCommand(closeDoor);
    }

    public void ReleaseOD() {
        //删除监听
        _Allocator.DeleteListener(this);
        _Allocator = null;
    }
    

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        System.out.println("开门成功");
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("<html>");
            strBuf.append("当前命令：");
            strBuf.append(cmd.getCommandParameter());
            strBuf.append("<br/>正在处理： ");
            strBuf.append(cmd.getProcessStep());
            strBuf.append(" / ");
            strBuf.append(cmd.getProcessMax());
            strBuf.append("</html>");
            System.out.println(strBuf.toString());
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
