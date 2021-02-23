package com.snk.door.demo;//import javax.swing.JOptionPane;
import zkteco.id100com.jni.id100sdk;

import java.io.*;

public class testDemo
{
	
	
	public static void main(String[] args)
	{
		//JOptionPane.showMessageDialog(null,
		//		"等待调试!", "系统信息", JOptionPane.INFORMATION_MESSAGE);
		//----读身份证测试
		//id100sdk.SetTemDir(""); //设置可以不保存临时文件
		System.out.println("开始调试，回车继续");
		try {
			System.in.read();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String path = id100sdk.getPath();
		if(id100sdk.InitCommExt() <= 0)
		{
			System.out.println("InitCommExt fail");
			return;
		}
		System.out.println("InitCommExt succ");
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(id100sdk.Authenticate() != 1)
			{
				System.out.println("Authenticate fail");
				return;
			}
			System.out.println("Authenticate succ");
			//循环读取
			long tickStart = System.currentTimeMillis();
			int ret = 0;
			while((ret=id100sdk.ReadContent(1)) != 1 && System.currentTimeMillis() < 2000)
			{
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ret != 1)
			{
				System.out.println("ReadContent fail");
				return;
			}
			
			System.out.println("ReadContent succ");
			System.out.println("输出文件路径：" + path);
			System.out.println("GetBmpPhotoExt ret=" + id100sdk.GetBmpPhotoExt());
			System.out.println("\n");
			System.out.println("姓名="+id100sdk.getName());
			System.out.println("民族="+id100sdk.getNation());
			System.out.println("性别="+id100sdk.getSex());
			System.out.println("身份证号 ="+ id100sdk.getIDNum());
			System.out.println("出生日期="+ id100sdk.getBirthdate());
			System.out.println("常住地址="+ id100sdk.getAddress());
			System.out.println("签发机关="+ id100sdk.getIssue());
			System.out.println("有效期="+ id100sdk.getEffectedDate() +"-" +id100sdk.getExpireDate());
			//System.out.printf("bmp base64头像=%s\n", id100sdk.getBMPPhotoBase64());
			//System.out.printf("jpg base64头像=%s\n", id100sdk.getJPGPhotoBase64());
			
		}
		
		//读身份证测试结束
		/*
		//IC卡测试
		int nPort = 8;	//串口号
		System.out.printf("发卡器版本:"+ id100sdk.ICGetDevVersion(nPort));
		
		int snr[] = new int[1];
		if (1 == id100sdk.ICGetICSnr(nPort, snr))
		{
			System.out.printf("读取IC卡卡号成功,卡号为"+ snr[0]);
		}
		else
		{
			System.out.printf("读取IC卡卡号失败");
		}
		System.out.printf("身份证物理卡号"+ id100sdk.ICGetIDSnr(nPort));
		System.out.printf("读IC卡数据："+ id100sdk.ICReadData(nPort, 0, 2, 0, "FFFFFFFFFFFF", snr));
		if(1 == id100sdk.ICWriteData(nPort, 0, 2, 0, "FFFFFFFFFFFF", "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", snr))
		{
			System.out.printf("写卡成功");
		}
		else
		{
			System.out.printf("写卡失败");
		}
		
		//HID 语音测试
		id100sdk.HIDVoice(0);
		*/
	}
}