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
import Door.Access.Door8800.Command.Card.ClearCardDataBase;
import Door.Access.Door8800.Command.Card.Parameter.ClearCardDataBase_Parameter;
import Door.Access.Door8800.Command.Card.Parameter.DeleteCard_Parameter;
import Door.Access.Door8800.Command.Card.Parameter.WriteCardListBySequence_Parameter;
import Door.Access.Door8800.Command.Card.Parameter.WriteCardListBySort_Parameter;
import Door.Access.Door89H.Command.Card.WriteCardListBySequence;
import Door.Access.Door89H.Command.Card.WriteCardListBySort;
import Door.Access.Door89H.Command.Data.CardDetail;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Door89H.Command.Card.DeleteCard;
import Door.Access.Util.ByteUtil;


import java.util.*;
import java.util.concurrent.Semaphore;
/**
 *
 * @author F
 */
public class UploadCard  implements INConnectorEvent{
    private ConnectorAllocator _Allocator;
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

    public UploadCard(ConnectorAllocator global) {
        if (global != null) {
            _Allocator = global;
        }else {
            System.out.println("命令对象不能为空");
            return;
        }        
        //添加监听
        _Allocator.AddListener(this);
    }
    //非排序区
    

    
    //排序区
   




    public void DeleteCardsss(){
        CommandDetail commandDetail = new CommandDetail();
        TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.1.167", 9788);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
        commandDetail.Identity = new Door8800Identity("MC-5924T20064155", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        commandDetail.Timeout = 5000;
        String[] listStrings= {"9788","12345678","556677"};
        DeleteCard_Parameter parameter=new DeleteCard_Parameter(commandDetail, listStrings);
        DeleteCard card=new  DeleteCard(parameter);
        _Allocator.AddCommand(card);
    }

    //清空卡片列表
    public void ClearCards(){
        //创建连接信息对象
        CommandDetail commandDetail = new CommandDetail();
        TCPClientDetail tcpClientDetail = new TCPClientDetail("192.168.1.65", 8000);//IP地址，端口(默认8000)
        commandDetail.Connector = tcpClientDetail;
        commandDetail.Identity = new Door8800Identity("AB-8940H48120001", "FFFFFFFF", E_ControllerType.Door8900);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        commandDetail.Timeout = 5000;
        ClearCardDataBase_Parameter par = new ClearCardDataBase_Parameter(commandDetail, 3);//初始化命令参数对象
        ClearCardDataBase cmd = new ClearCardDataBase(par);//初始化命令对象
        //添加命令到队列
        _Allocator.AddCommand(cmd);
    }

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
      //  WriteCardListBySequence_Result res =  (WriteCardListBySequence_Result) cmd;
       // ArrayList<? extends Door.Access.Door8800.Command.Data.CardDetail> a=res.CardList;
        //DeleteCard_Parameter res=(DeleteCard_Parameter)cmd;
        //删除卡，有返回到这里就说明成功。


        System.out.println("doorAccessiodemo.frmMain.CommandCompleteEvent() -- 执行命令成功");
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("<html>");
            strBuf.append("当前命令：");
            //strBuf.append(GetCommandName(cmd));
            strBuf.append("<br/>正在处理： ");
            strBuf.append(cmd.getProcessStep());
            strBuf.append(" / ");
            strBuf.append(cmd.getProcessMax());
            strBuf.append("</html>");
             //System.out.println(strBuf.toString());
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
            };
             System.out.println(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.ConnectorErrorEvent() --- " + e.toString());
        }

    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detial) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            //strBuf.append("网络通道故障，IP信息：");
             System.out.println(strBuf.toString());
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
             System.out.println(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.CommandTimeout() -- " + e.toString());
        }

    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        try {
             System.out.println("doorAccessiodemo.frmMain.PasswordErrorEvent() -- 通讯密码错误" );  
        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.PasswordErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        try {
             System.out.println("doorAccessiodemo.frmMain.ChecksumErrorEvent() -- 命令返回的校验和错误" );  
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
           // System.out.println(strBuf.toString());
            strBuf = null;

        } catch (Exception e) {
            System.out.println("doorAccessiodemo.frmMain.WatchEvent() -- " + e.toString());
        }
    }
    @Override
    public void ClientOnline(TCPServerClientDetail client) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ClientOffline(TCPServerClientDetail client) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
