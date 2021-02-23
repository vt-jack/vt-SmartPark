package com.snk.door.api.finger.zk;

import com.alibaba.fastjson.JSON;
import com.snk.common.config.Global;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.utils.file.FileUtils;
import com.snk.common.utils.security.PermissionUtils;
import com.snk.door.socketio.ISocketIOService;
import com.snk.door.socketio.entity.Message;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 指纹仪实现类
 */
@Component
public class FingerReaderZkService {

    private static final Logger log = LoggerFactory.getLogger(FingerReaderZkService.class);

    @Autowired
    private ISocketIOService socketIOService;

    //the width of fingerprint image
    private int fpWidth = 0;
    //the height of fingerprint image
    private int fpHeight = 0;

    private int nFakeFunOn = 1;

    private byte[] imgbuf = null;

    private boolean mbStop = true;
    private long mhDevice = 0;
    private long mhDB = 0;
    private WorkThread workThread = null;

    public String open() {
        if (0 != mhDevice)
        {
            return "操作过于频繁，请稍后再试";
        }
        int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;
        if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init())
        {
            return "初始化指纹仪失败";
        }
        ret = FingerprintSensorEx.GetDeviceCount();
        if (ret < 0)
        {
            freeSensor();
            return "无法连接到指纹仪";
        }
        if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0)))
        {
            log.info("Open device fail, ret = " + ret + "!");
            freeSensor();
            return "打开指纹仪失败";
        }
        if (0 == (mhDB = FingerprintSensorEx.DBInit()))
        {
            log.info("Init DB fail, ret = " + ret + "!");
            freeSensor();
            return "初始化数据库失败";
        }

        byte[] paramValue = new byte[4];
        int[] size = new int[1];

        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
        fpWidth = byteArrayToInt(paramValue);
        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
        fpHeight = byteArrayToInt(paramValue);
        imgbuf = new byte[fpWidth*fpHeight];
        mbStop = false;
        workThread = new WorkThread();
        workThread.start();// 启动线程
        log.info("open success");
        return null;
    }

    public void close() {
        freeSensor();
    }

    private class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            int ret = 0;
            while (!mbStop) {
                if (0 == FingerprintSensorEx.AcquireFingerprintImage(mhDevice, imgbuf))
                {
                    Long userId = (Long) PermissionUtils.getPrincipalProperty("userId");
                    // 发送给前端的消息体
                    Message message = new Message(userId);
                    // 返回数据
                    Map<String, Object> map = new HashMap<>();
                    if (nFakeFunOn == 1)
                    {
                        byte[] paramValue = new byte[4];
                        int[] size = new int[1];
                        size[0] = 4;
                        int nFakeStatus = 0;
                        //GetFakeStatus
                        ret = FingerprintSensorEx.GetParameters(mhDevice, 2004, paramValue, size);
                        nFakeStatus = byteArrayToInt(paramValue);
                        if (0 == ret && (byte)(nFakeStatus & 31) != 31)
                        {
                            map.put(AjaxResult.CODE_TAG, AjaxResult.Type.ERROR.value());
                            map.put(AjaxResult.DATA_TAG, "Is a fake-finer?");
                            message.setContent(JSON.toJSONString(map));
                            socketIOService.pushMessage(Message.FINGER_READER, message);
                            return;
                        }
                    }
                    try {
                        String filePath = Global.getProfile() + "/fingerTemplate-" + userId + ".png";
                        writeBitmap(imgbuf, fpWidth, fpHeight, filePath);   // 上传文件
                        byte[] bytes = FileUtils.readImage(filePath);   // 读取文件
                        message.setBytes(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    map.put(AjaxResult.CODE_TAG, AjaxResult.Type.SUCCESS.value());
                    message.setContent(JSON.toJSONString(map));
                    socketIOService.pushMessage(Message.FINGER_READER, message);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void freeSensor()
    {
        mbStop = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (0 != mhDB)
        {
            FingerprintSensorEx.DBFree(mhDB);
            mhDB = 0;
        }
        if (0 != mhDevice)
        {
            FingerprintSensorEx.CloseDevice(mhDevice);
            mhDevice = 0;
        }
        FingerprintSensorEx.Terminate();
        log.info("close success");
    }

    public static int byteArrayToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }

    public static void writeBitmap(byte[] imageBuf, int nWidth, int nHeight,
                                   String path) throws IOException {
        java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

        int w = (((nWidth+3)/4)*4);
        int bfType = 0x424d; // // 位图文件类型（0—1字节）
        int bfSize = 54 + 1024 + w * nHeight;// bmp文件的大小（2—5字节）
        int bfReserved1 = 0;// 位图文件保留字，必须为0（6-7字节）
        int bfReserved2 = 0;// 位图文件保留字，必须为0（8-9字节）
        int bfOffBits = 54 + 1024;// 文件头开始到位图实际数据之间的字节的偏移量（10-13字节）

        dos.writeShort(bfType); // 输入位图文件类型'BM'
        dos.write(changeByte(bfSize), 0, 4); // 输入位图文件大小
        dos.write(changeByte(bfReserved1), 0, 2);// 输入位图文件保留字
        dos.write(changeByte(bfReserved2), 0, 2);// 输入位图文件保留字
        dos.write(changeByte(bfOffBits), 0, 4);// 输入位图文件偏移量

        int biSize = 40;// 信息头所需的字节数（14-17字节）
        int biWidth = nWidth;// 位图的宽（18-21字节）
        int biHeight = nHeight;// 位图的高（22-25字节）
        int biPlanes = 1; // 目标设备的级别，必须是1（26-27字节）
        int biBitcount = 8;// 每个像素所需的位数（28-29字节），必须是1位（双色）、4位（16色）、8位（256色）或者24位（真彩色）之一。
        int biCompression = 0;// 位图压缩类型，必须是0（不压缩）（30-33字节）、1（BI_RLEB压缩类型）或2（BI_RLE4压缩类型）之一。
        int biSizeImage = w * nHeight;// 实际位图图像的大小，即整个实际绘制的图像大小（34-37字节）
        int biXPelsPerMeter = 0;// 位图水平分辨率，每米像素数（38-41字节）这个数是系统默认值
        int biYPelsPerMeter = 0;// 位图垂直分辨率，每米像素数（42-45字节）这个数是系统默认值
        int biClrUsed = 0;// 位图实际使用的颜色表中的颜色数（46-49字节），如果为0的话，说明全部使用了
        int biClrImportant = 0;// 位图显示过程中重要的颜色数(50-53字节)，如果为0的话，说明全部重要

        dos.write(changeByte(biSize), 0, 4);// 输入信息头数据的总字节数
        dos.write(changeByte(biWidth), 0, 4);// 输入位图的宽
        dos.write(changeByte(biHeight), 0, 4);// 输入位图的高
        dos.write(changeByte(biPlanes), 0, 2);// 输入位图的目标设备级别
        dos.write(changeByte(biBitcount), 0, 2);// 输入每个像素占据的字节数
        dos.write(changeByte(biCompression), 0, 4);// 输入位图的压缩类型
        dos.write(changeByte(biSizeImage), 0, 4);// 输入位图的实际大小
        dos.write(changeByte(biXPelsPerMeter), 0, 4);// 输入位图的水平分辨率
        dos.write(changeByte(biYPelsPerMeter), 0, 4);// 输入位图的垂直分辨率
        dos.write(changeByte(biClrUsed), 0, 4);// 输入位图使用的总颜色数
        dos.write(changeByte(biClrImportant), 0, 4);// 输入位图使用过程中重要的颜色数

        for (int i = 0; i < 256; i++) {
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(0);
        }

        byte[] filter = null;
        if (w > nWidth)
        {
            filter = new byte[w-nWidth];
        }

        for(int i=0;i<nHeight;i++)
        {
            dos.write(imageBuf, (nHeight-1-i)*nWidth, nWidth);
            if (w > nWidth)
                dos.write(filter, 0, w-nWidth);
        }
        dos.flush();
        dos.close();
        fos.close();
    }

    public static byte[] changeByte(int data) {
        return intToByteArray(data);
    }

    public static byte[] intToByteArray (final int number) {
        byte[] abyte = new byte[4];
        abyte[0] = (byte) (0xff & number);
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

}
