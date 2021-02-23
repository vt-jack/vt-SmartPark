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
import Door.Access.Data.AbstractTransaction;
import Door.Access.Data.BytesData;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Door8800.Packet.Door8800Decompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Packet.INPacketModel;
import Door.Access.Packet.PacketDecompileAllocator;
import Face.Data.FaceCommandWatchResponse;
import Face.System.BeginWatch;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TcpServerTest implements INConnectorEvent {

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

    public TcpServerTest() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _Allocator.Listen("172.29.58.219", 9010);
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
        if (event instanceof BytesData) {
            authentication(detail,event);
        }else {
            Transaction(detail,event);
        }
    }

    /**
     * 身份验证
     * @param detail
     * @param event
     */
    private void authentication(ConnectorDetail detail, INData event) {
        BytesData b = (BytesData) event;
        TCPServerClientDetail cd = (TCPServerClientDetail) detail;
        ByteBuf dBuf = b.GetBytes();
        if (dBuf.getByte(0) == 0x7e) {
            Door8800Decompile decompile = new Door8800Decompile();
            ArrayList<INPacketModel> oRetPack = new ArrayList<>(10);
            if (decompile.Decompile(dBuf, oRetPack)) {
                Door8800PacketModel m = (Door8800PacketModel) oRetPack.get(0);
                if(m.GetSN().contains("SN-4024T")){
                    _Allocator.AddWatchDecompile(cd, PacketDecompileAllocator.GetDecompile(E_ControllerType.Door8900));
                    System.out.println("添加控制板解析器");
                }else {
                    //添加人脸解析器
                    _Allocator.AddWatchDecompile(cd, new FaceCommandWatchResponse());
                    if(m.GetSN().contains("FC-8100T")) {
                        CommandDetail commandDetail = new CommandDetail();
                        commandDetail.Connector = detail;
                        commandDetail.Identity=new Door8800Identity(m.GetSN(),Long.toHexString(m.GetPassword()),E_ControllerType.Door8900);
                        beginWatch(commandDetail);
                        System.out.println("添加到数据监控");
                    }
                    System.out.println("添加人脸解析器");
                }
                // m.GetSN() -- 这个就是此控制器的SN号，
                System.out.println("客户端ID:" + cd.ClientID + "(" + m.GetSN() + ")，收到数据包：" + ByteBufUtil.hexDump(dBuf));
            }

        }
    }

    public  void beginWatch( CommandDetail commandDetail){

        CommandParameter parameter=new  CommandParameter(commandDetail);
        BeginWatch cmd=new BeginWatch(parameter);
        _Allocator.AddCommand(cmd);
    }
    /**
     * 监控消息处理
     * @param detail
     * @param event
     */
    private  void Transaction(ConnectorDetail detail, INData event){
        if (event instanceof Door8800WatchTransaction) {
            Door8800WatchTransaction watchEvent = (Door8800WatchTransaction) event;
            AbstractTransaction tr = (AbstractTransaction) watchEvent.EventData;
            System.out.println(watchEvent.SN+" 收到监控事件：" + tr.getClass().toString());
//            switch (watchEvent.CmdIndex) {
//                case 1://认证记录
//                    CardTransaction card = (CardTransaction) watchEvent.EventData;
//
//                    break;
//                case 2://门磁记录
//                    DoorSensorTransaction DoorSensor = (DoorSensorTransaction) watchEvent.EventData;
//                    break;
//                case 3://系统记录
//                    SystemTransaction system = (SystemTransaction) watchEvent.EventData;
//                    break;
//                case 4://包活/连接测试记录
//                    System.out.println("保活包消息.................");
//                    break;
//                default:
//                    DefinedTransaction dt = (DefinedTransaction) watchEvent.EventData;
//
//            }
        } else {
            System.out.println("testio.FCardIO.FCardIOTest.WatchEvent() -- 未知消息");
        }
    }

    @Override
    public void ClientOnline(TCPServerClientDetail client) {
        // client.
        System.out.println("有客户端上线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
    }

    @Override
    public void ClientOffline(TCPServerClientDetail client) {
        System.out.println("有客户端离线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
    }
}
