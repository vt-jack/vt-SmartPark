/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.snk.door.demo;
import Door.Access.Connector.ConnectorAllocator;

/**
 *
 * @author F
 */
public class JavaDemo {
        public static void main(String[] args) {
        // TODO code application logic here
        //门相关操作
        ConnectorAllocator a = ConnectorAllocator.GetAllocator();
        //数据监控
        //DataMonitor dm = new DataMonitor(a);
        //dm.OpenMonitor();

            OpenDoor od = new OpenDoor(a);

        od.OpenDoor();
        //卡片相关操作
//        UploadCard uc = new UploadCard(a);
//        uc.DeleteCardsss();
//        uc.syn();
     //   uc.UploadSortCardList();
     //   uc.ClearCards();
        //时间相关操作
     //   SetDateTime sdt = new SetDateTime(a);
     //   sdt.Set();
     //   sdt.SetCustomize();
      //  sdt.Get();
     //   ReadRecord  readRecord=new  ReadRecord(a);
      //  readRecord.Read();
       // System.out.println("");
      //  byte[] b = new byte[10];
        //System.in.read();
        //a.Release();
                //ReadSn read = new ReadSn(a);
                //read.GetSn();

    }
}
