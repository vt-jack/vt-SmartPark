package com.snk.door.demo;

import com.snk.door.api.finger.yz.BioMiniLibrary;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class DemoTestZwy {

    /**
     * 上下文Id
     */
    static int iContextId;
    /**
     * 返回成功
     */
    private static final int PIS_FP_OK = 0;
    /**
     * 临时模板尺寸
     */
    static int iTemplateSize;
    /**
     * 正式模板尺寸
     */
    static int iFeatureSize;
    /**
     * 图片宽度
     */
    static int iImaWidth;
    /**
     * 图片高度
     */
    static int iImaHeight;
    /**
     * 图像扫描精度
     */
    static int iImaRes;
    /**
     * 设备Id
     */
    static String bDevId;
    /**
     * 设备名称
     */
    static String sDevName;
    /**
     * 基础功能调用实例
     */
    static BioMiniLibrary.Ast2600N ast2600N;
    /**
     * 数据转换实例
     */
    static BioMiniLibrary.FpDataConv fpDataConv;

    /**
     * 执行方法前需要先完成如下步骤：
     * 1、获取设备信息
     * 2、创建上下文
     * 3、打开设备连接
     *
     * @param args
     */
    public static void main(String[] args) {
        ast2600N = BioMiniLibrary.Ast2600N.instance;
        fpDataConv = BioMiniLibrary.FpDataConv.instance;
        registrationProcess();
        /*fingerprintCheck();
        fpConv();
        soundPlay();
        readCardProcess();
        contrastTptArray();
        close();*/
    }

    public static void close(){
        ast2600N.pisCloseDevice(iContextId);
        ast2600N.pisDestroyContext(iContextId);
    }

    /**
     * 登记流程
     */
    public static void registrationProcess() {
        System.out.println("开始登记流程");
        getDevice();
        createContext();
        openDevice();
        getInfo();
        try {
            regEnroll();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("结束登记流程");
    }

    /**
     * 读卡流程
     */
    public static void readCardProcess() {
        System.out.println("开始读卡流程");
        getDevice();
        createContext();
        openDevice();
        ReadCard();
        System.out.println("结束读卡流程");
    }

    /**
     * 获取一个设备
     */
    public static void getDevice() {
        byte[] devId = new byte[100];
        byte[] devName = new byte[100];
        System.out.println("获取一个设备---------------------------------------------");
        for (int i = 0; i < 5; i++) {
            int go = ast2600N.pisEnumerateDevice(i, devId, devName);
            if (go == 0) {
                sDevName = new String(devName);
                bDevId = new String(devId);
                System.out.println("设备名称：" + sDevName);
                return;
            }
        }
    }

    /**
     * 创建上下文
     */
    public static void createContext() {
        IntByReference contextId = new IntByReference();
        int iRet = ast2600N.pisCreateContext(contextId);
        System.out.println("创建上下文---------------------------------------------");
        if (iRet == PIS_FP_OK) {
            iContextId = contextId.getValue();
            System.out.println("创建上下文成功！");
        } else {
            System.out.println("创建上下文失败！");
        }
    }

    /**
     * 打开设备连接
     */
    public static void openDevice() {
        int iRet = ast2600N.pisOpenDevice(iContextId, bDevId.getBytes());
        System.out.println("打开设备---------------------------------------------");
        if (iRet == PIS_FP_OK) {
            System.out.println("打开设备连接成功！");
        } else {
            System.out.println("打开设备连接失败！");
        }
    }

    /**
     * 获取指纹仪相关信息
     */
    public static void getInfo() {
        byte[] bEngineInfo = new byte[100];
        IntByReference imaWidth = new IntByReference();
        IntByReference imaHeight = new IntByReference();
        IntByReference imaRes = new IntByReference();
        IntByReference featureSize = new IntByReference();
        IntByReference templateSize = new IntByReference();
        int iRet = ast2600N.pisGetInfo(iContextId, bEngineInfo, imaWidth, imaHeight, imaRes, featureSize, templateSize);
        if (iRet == PIS_FP_OK) {
            iImaWidth = imaWidth.getValue();
            iImaHeight = imaHeight.getValue();
            iImaRes = imaRes.getValue();
            iFeatureSize = featureSize.getValue();
            iTemplateSize = templateSize.getValue();
            System.out.println("指纹相关信息---------------------------------------------");
            System.out.println("算  法 引 擎: " + new String(bEngineInfo));
            System.out.println("图  像 尺 寸: " + iImaWidth + "*" + iImaHeight);
            System.out.println("图像扫描精度: " + iImaRes + "DPI");
            System.out.println("临时模板尺寸: " + iFeatureSize);
            System.out.println("正式模板尺寸: " + iTemplateSize);
        }
    }

    /**
     * 登记指纹
     */
    public static void regEnroll() throws UnsupportedEncodingException {
        /**
         * 登记步骤
         * 1、获取一个相机
         * 2、判断是否返回指纹图像
         * 3、保存指纹特征码
         * 4、判断指纹是否离开，重复以上的步骤
         * 5、输入完3次指纹之后计算指纹特征码
         */
        System.out.println("开始登记指纹----------------------------------");
        int iRet = ast2600N.pisSoundPlay(iContextId, BioMiniLibrary.Sound.请按指纹.ordinal());
        if (iRet != PIS_FP_OK) {
            System.out.println("语音播报失败");
            return;
        }
        ArrayList<Pointer> aEnrollFeature1 = new ArrayList<Pointer>();//临时特征码存储集合
        boolean bEnrollLeave = true;//指纹移开标志
        while (true) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int size = iImaWidth * iImaHeight;
            Pointer pImageBuf = new Memory(size);
            iRet = ast2600N.pisCapture(iContextId, pImageBuf);
            if (iRet != PIS_FP_OK) {
                System.out.println("获取一个活体相机失败");
                return;
            }

            //判断是否按下指纹
            IntByReference iIsCheck = new IntByReference(0);//是否获取到指纹
            IntByReference iFpArea = new IntByReference(0);//指纹清晰度
            iRet = ast2600N.pisCheckFp(iContextId, pImageBuf, iImaWidth, iImaHeight, iImaRes, iIsCheck, iFpArea);
            boolean isFpArea = iFpArea.getValue() < 40;
            if (iRet != PIS_FP_OK) {
                System.out.println("判断是否按下指纹失败");
                return;
            }
            if (iIsCheck.getValue() <= 0) {
                bEnrollLeave = true;
                continue;
            }
            if (!bEnrollLeave) continue;//判断指纹是否离开
            bEnrollLeave = false;
            if (isFpArea) {//图片不清晰
                ast2600N.pisSoundPlay(iContextId, BioMiniLibrary.Sound.请重新按指纹.ordinal());
                continue;
            }
            Pointer bTemp = new Memory(iFeatureSize);
            iRet = ast2600N.pisProcess(iContextId, pImageBuf, iImaWidth, iImaHeight, iImaRes, bTemp);
            if (iRet != PIS_FP_OK) {
                System.out.println("加工指纹图片失败");
                return;
            }
            aEnrollFeature1.add(bTemp);
            //保存图片
            int iListSize = aEnrollFeature1.size();
            /*String sPath = System.getProperty("user.dir") + "\\指纹图片" + iListSize + ".jpg";
            Pointer pPath = new Memory(sPath.length() * 2);
            pPath.setString(0, sPath);
            iRet = BioMiniLibrary.RC4.instance.SaveGrayBitmapToFile(pImageBuf, iImaWidth, iImaHeight, pPath);
            if (iRet == 1) {
                System.out.println("图片保存成功：" + sPath);
            }*/
            //判断是否已经输入3次指纹
            if (iListSize == 3) {
                Pointer bTemplate = new Memory(iTemplateSize);//指纹特征码存储数组
                iRet = ast2600N.pisCreateTemplate(iContextId, aEnrollFeature1.get(0), aEnrollFeature1.get(1), aEnrollFeature1.get(2), bTemplate);
                if (iRet == PIS_FP_OK) {
                    ast2600N.pisSoundPlay(iContextId, BioMiniLibrary.Sound.登记成功.ordinal());
                    System.out.println("指纹录入成功！");
                    printBase64String(bTemplate.getByteArray(0, iTemplateSize));
                    return;
                } else {
                    //生成失败重新录入
                    aEnrollFeature1.clear();
                    ast2600N.pisSoundPlay(iContextId, BioMiniLibrary.Sound.请重新按指纹.ordinal());
                }
            } else {
                ast2600N.pisSoundPlay(iContextId, BioMiniLibrary.Sound.请再次按指纹.ordinal());
                System.out.println("请按指纹" + iListSize + "/3");
            }
        }
    }

    /**
     * 指纹数据检测
     */
    public static void fingerprintCheck() {
        String sBase64Fp = "hQWQAUECtiMUPltaAABAAAEDBgQKCwIDBgkADQgHEAwADwoQdtTc0FVN5bOZcBcXEzAZGhQhHSJ+Y1P0UIxrRBoOAgMEBwUGDg8ABggJAQIJCg8QAw0SBQIAAQ4MCwgGCRIRDwAHEwUCBAEAAQQFBwMOBg0ICQYAAQoICRARDwMSCBIKBg8RAwAAAAAAAAAAAAAAAAAAAAAAAAAAlKumhn6Fq6yimMK4n3RvgrjYsb8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH5/iGCYoUymOy86vsZRmD8jOhemAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACEAACFAQAHRICEBkWBBQCAAASHQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAAEBAAEAAQABAQEBAAMFBQUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP////////////////+q////////D6D///////8PoPX//////wBQ9f////8/AFDF/////w8AEKD6////DwAAoPr///8PAACg+v///w8AAAD/////DwAAAPX///8PAAAA9f///w8AAAD/////DwAAAP////8PAAAA/////w8AAND/////CgAA4P////8KAADg/////wAAAPD/////AACg+v////8AAKD6/////wAAoPr/////AAAA8P////+fAADw//////8AAPD/////PwAA////////AAD///////8vwP////////////////////////////////////////////8AAJxsARgYAAAAAAQAAAAAAAAAAOyQ9tPprhW8tOf8C/PV7d5R6wIEN+4cJjEtHQwNYRtlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKzwkagkqwd1f4v4UdtVbg+csX8ILwtrSeAMy5llW8HrlU0dAhIYKdux+Kah1Mt5RcwZkSJ0ak4mx4DzyrVNcYc6OH39A+WlXfXaasiqJj/m0vg7HEteUy3o1iy32ePySol3TcfYNybosyyPxkwZinGIR+opp9HZnar93gUDGcdlWvz3qfkCDDJ+y+3k9lr94tDr9+7iapWz4oyPLMP/a3+W/zfUcHIbnJ2cV+0Z52Po8fiGSnnI/pNFFWsgMi311furwOCmgCzXP6dpmO5qTfWhHLyciG+QQwbcUMoK1H4iCW7jr1Z+9E8idXmTlVdAEfNH3/Xx90XF0ZPKM2NaDeBBqOZbDqecDPhJmfMqAtW5CQf1ZtrXoWHs1nr/KgcXgOtG4KABo5YjA/pQ1FaTWIjUceyJ0ot/cyt3EI0od1pz24CALX3Fr57RGDtE4lZ1CHVOy+WYMcf71cIlruBoqXd5fr29LWHVA4YfZovVnN/ISM1YIpZZlcRaHlTgSUipM5WR6xe1R9iRkwpiegL4xaArhaTCiF7skonNShQDbSAvfFlshEMz37lm+xOJoL+msuDe5SS9lKZoMt9fFe8ZXTCN7Jm8cDppyoIKgMqRf200gPR7MZpSiOV9VyaAx7GT0BbwOJNYc9OFRT33qRnKXdarS+vZmXWSzA7d0VyC0iC/UTCJsq1GqdrpRjyNeq7hei1n3nBoJDWU2wDtaV6xlu4aeXUODoqDRcjq5aCBPVWe51tiP4o0RIKtwrRkOfLZwAkenY2PgrPc0g4Vl40oBELhTXjV9VEZedib2Ds=";
        byte[] sModel = new byte[50];
        int iRet = fpDataConv.FPCONV_GetConvDLLModel(sModel);
        if (iRet == PIS_FP_OK) {
            System.out.println("动态库版本号:" + new String(sModel));
        }
        byte[] bValue = Base64StringFormBytes(sBase64Fp);
        Pointer sFpData = new Memory(bValue.length);
        sFpData.write(0, bValue, 0, bValue.length);
        IntByReference apnVersion = new IntByReference();
        IntByReference apnSize = new IntByReference();
        iRet = fpDataConv.FPCONV_GetFpDataVersionAndSize(sFpData, apnVersion, apnSize);
        if (iRet == PIS_FP_OK) {
            System.out.println("指纹版本号:" + getVersion(apnVersion.getValue()));
            System.out.println("指纹缓冲区长度:" + apnSize.getValue());
        }

    }

    /**
     * 获取指纹版本
     *
     * @param value 指纹版本数值
     * @return 指纹版本号
     */
    public static String getVersion(int value) {
        String result;
        switch (value) {
            case BioMiniLibrary.FpDataConv.DATA_VER_70:
                result = "VER_70";
                break;
            case BioMiniLibrary.FpDataConv.DATA_VER_80:
                result = "VER_80";
                break;
            case BioMiniLibrary.FpDataConv.DATA_VER_85:
                result = "VER_85";
                break;
            case BioMiniLibrary.FpDataConv.DATA_VER_89:
                result = "VER_89";
                break;
            default:
                result = "未知版本:" + value;
                break;
        }
        return result;
    }

    /**
     * 指纹数据版本转换
     */
    public static void fpConv() {
        int lDstVer = BioMiniLibrary.FpDataConv.DATA_VER_70;//目标版本
        String sBase64Fp = "hQWQAUECtiMUPltaAABAAAEDBgQKCwIDBgkADQgHEAwADwoQdtTc0FVN5bOZcBcXEzAZGhQhHSJ+Y1P0UIxrRBoOAgMEBwUGDg8ABggJAQIJCg8QAw0SBQIAAQ4MCwgGCRIRDwAHEwUCBAEAAQQFBwMOBg0ICQYAAQoICRARDwMSCBIKBg8RAwAAAAAAAAAAAAAAAAAAAAAAAAAAlKumhn6Fq6yimMK4n3RvgrjYsb8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH5/iGCYoUymOy86vsZRmD8jOhemAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACEAACFAQAHRICEBkWBBQCAAASHQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAAEBAAEAAQABAQEBAAMFBQUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP////////////////+q////////D6D///////8PoPX//////wBQ9f////8/AFDF/////w8AEKD6////DwAAoPr///8PAACg+v///w8AAAD/////DwAAAPX///8PAAAA9f///w8AAAD/////DwAAAP////8PAAAA/////w8AAND/////CgAA4P////8KAADg/////wAAAPD/////AACg+v////8AAKD6/////wAAoPr/////AAAA8P////+fAADw//////8AAPD/////PwAA////////AAD///////8vwP////////////////////////////////////////////8AAJxsARgYAAAAAAQAAAAAAAAAAOyQ9tPprhW8tOf8C/PV7d5R6wIEN+4cJjEtHQwNYRtlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKzwkagkqwd1f4v4UdtVbg+csX8ILwtrSeAMy5llW8HrlU0dAhIYKdux+Kah1Mt5RcwZkSJ0ak4mx4DzyrVNcYc6OH39A+WlXfXaasiqJj/m0vg7HEteUy3o1iy32ePySol3TcfYNybosyyPxkwZinGIR+opp9HZnar93gUDGcdlWvz3qfkCDDJ+y+3k9lr94tDr9+7iapWz4oyPLMP/a3+W/zfUcHIbnJ2cV+0Z52Po8fiGSnnI/pNFFWsgMi311furwOCmgCzXP6dpmO5qTfWhHLyciG+QQwbcUMoK1H4iCW7jr1Z+9E8idXmTlVdAEfNH3/Xx90XF0ZPKM2NaDeBBqOZbDqecDPhJmfMqAtW5CQf1ZtrXoWHs1nr/KgcXgOtG4KABo5YjA/pQ1FaTWIjUceyJ0ot/cyt3EI0od1pz24CALX3Fr57RGDtE4lZ1CHVOy+WYMcf71cIlruBoqXd5fr29LWHVA4YfZovVnN/ISM1YIpZZlcRaHlTgSUipM5WR6xe1R9iRkwpiegL4xaArhaTCiF7skonNShQDbSAvfFlshEMz37lm+xOJoL+msuDe5SS9lKZoMt9fFe8ZXTCN7Jm8cDppyoIKgMqRf200gPR7MZpSiOV9VyaAx7GT0BbwOJNYc9OFRT33qRnKXdarS+vZmXWSzA7d0VyC0iC/UTCJsq1GqdrpRjyNeq7hei1n3nBoJDWU2wDtaV6xlu4aeXUODoqDRcjq5aCBPVWe51tiP4o0RIKtwrRkOfLZwAkenY2PgrPc0g4Vl40oBELhTXjV9VEZedib2Ds=";
        byte[] bValue = Base64StringFormBytes(sBase64Fp);
        Pointer sFpData = new Memory(bValue.length);
        sFpData.write(0, bValue, 0, bValue.length);
        int iRet = fpDataConv.FPCONV_GetFpDataValidity(sFpData);
        if (iRet != PIS_FP_OK) {
            System.out.println("指纹验证失败!");
            return;
        }
        IntByReference apnVersion = new IntByReference();
        IntByReference apnSize = new IntByReference();
        iRet = fpDataConv.FPCONV_GetFpDataVersionAndSize(sFpData, apnVersion, apnSize);
        if (iRet != PIS_FP_OK) {
            System.out.println("获取指纹信息失败!");
            return;
        }
        if (lDstVer == apnVersion.getValue()) {
            System.out.println("版本一致无需转换!");
            return;
        }
        iRet = fpDataConv.FPCONV_GetFpDataSize(lDstVer, apnSize);
        if (iRet != PIS_FP_OK) {
            System.out.println("获取转换信息失败");
        }
        Pointer bDstFP = new Memory(apnSize.getValue());
        iRet = fpDataConv.FPCONV_Convert(apnVersion.getValue(), sFpData, lDstVer, bDstFP);
        if (iRet == PIS_FP_OK) {
            System.out.println("数据转换成功，版本号为：" + getVersion(lDstVer));
            printBase64String(bDstFP.getByteArray(0, apnSize.getValue()));
        }
    }

    /**
     * 播放语音文件
     */
    public static void soundPlay() {
        //播放语音需要先获取设备信息
        //  getDevice();
        //  createContext();
        //  openDevice();
        ast2600N.pisSoundPlay(iContextId, BioMiniLibrary.Sound.请按指纹.ordinal());
    }

    /**
     * 读卡
     */
    public static void ReadCard() {

        int iRet = ast2600N.pisScanCard(iContextId, 1);
        if (iRet != PIS_FP_OK) {
            System.out.println("读卡失败");
            return;
        }
        byte[] cardNum = new byte[100];
        while (true) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iRet = ast2600N.pisGetCardNumber(iContextId, cardNum);
            if (iRet == PIS_FP_OK) {
                System.out.println("卡号：" + new String(cardNum));
                return;
            }
        }
    }

    /**
     * 对比指纹数据
     */
    public static void contrastTptArray() {

        /**
         * 1、校验数据是否正确
         * 2、先校验指纹数据版本
         * 3、将需要对比指纹的数据添加到缓冲区
         * 4、对比缓冲区指纹数据
         */
        getDevice();
        createContext();
        openDevice();
        String sFpData1 = "hQWQAUECtiMUPltaAABAAAEDBgQKCwIDBgkADQgHEAwADwoQdtTc0FVN5bOZcBcXEzAZGhQhHSJ+Y1P0UIxrRBoOAgMEBwUGDg8ABggJAQIJCg8QAw0SBQIAAQ4MCwgGCRIRDwAHEwUCBAEAAQQFBwMOBg0ICQYAAQoICRARDwMSCBIKBg8RAwAAAAAAAAAAAAAAAAAAAAAAAAAAlKumhn6Fq6yimMK4n3RvgrjYsb8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH5/iGCYoUymOy86vsZRmD8jOhemAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACEAACFAQAHRICEBkWBBQCAAASHQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBAAEBAAEAAQABAQEBAAMFBQUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP////////////////+q////////D6D///////8PoPX//////wBQ9f////8/AFDF/////w8AEKD6////DwAAoPr///8PAACg+v///w8AAAD/////DwAAAPX///8PAAAA9f///w8AAAD/////DwAAAP////8PAAAA/////w8AAND/////CgAA4P////8KAADg/////wAAAPD/////AACg+v////8AAKD6/////wAAoPr/////AAAA8P////+fAADw//////8AAPD/////PwAA////////AAD///////8vwP////////////////////////////////////////////8AAJxsARgYAAAAAAQAAAAAAAAAAOyQ9tPprhW8tOf8C/PV7d5R6wIEN+4cJjEtHQwNYRtlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKzwkagkqwd1f4v4UdtVbg+csX8ILwtrSeAMy5llW8HrlU0dAhIYKdux+Kah1Mt5RcwZkSJ0ak4mx4DzyrVNcYc6OH39A+WlXfXaasiqJj/m0vg7HEteUy3o1iy32ePySol3TcfYNybosyyPxkwZinGIR+opp9HZnar93gUDGcdlWvz3qfkCDDJ+y+3k9lr94tDr9+7iapWz4oyPLMP/a3+W/zfUcHIbnJ2cV+0Z52Po8fiGSnnI/pNFFWsgMi311furwOCmgCzXP6dpmO5qTfWhHLyciG+QQwbcUMoK1H4iCW7jr1Z+9E8idXmTlVdAEfNH3/Xx90XF0ZPKM2NaDeBBqOZbDqecDPhJmfMqAtW5CQf1ZtrXoWHs1nr/KgcXgOtG4KABo5YjA/pQ1FaTWIjUceyJ0ot/cyt3EI0od1pz24CALX3Fr57RGDtE4lZ1CHVOy+WYMcf71cIlruBoqXd5fr29LWHVA4YfZovVnN/ISM1YIpZZlcRaHlTgSUipM5WR6xe1R9iRkwpiegL4xaArhaTCiF7skonNShQDbSAvfFlshEMz37lm+xOJoL+msuDe5SS9lKZoMt9fFe8ZXTCN7Jm8cDppyoIKgMqRf200gPR7MZpSiOV9VyaAx7GT0BbwOJNYc9OFRT33qRnKXdarS+vZmXWSzA7d0VyC0iC/UTCJsq1GqdrpRjyNeq7hei1n3nBoJDWU2wDtaV6xlu4aeXUODoqDRcjq5aCBPVWe51tiP4o0RIKtwrRkOfLZwAkenY2PgrPc0g4Vl40oBELhTXjV9VEZedib2Ds=";
        String sFpData2 = "hVCQAdYDUDAbTF9dAABAABMAFgQGCAsRAhIDFQ0MBQ8ODQYBr0xnIruz7iF2eB4XGhQTGBoWFxmvQ8YGW0BdZpb5EgEGBwUWGAkUFwsOAQgDEQIEAQYTAQcWGBMNAAIDChITEAcXFAkAGhUZCRkVFwAaFBgHFgMEAgEDBRIHARMWAgADBBYGBwAAAAAAAAAAAAAAAAAAAAAAAAAAkpyDgrZjb5dhlFKxyqCfYWWyg46FkJyFpY2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGODj6CNjJuqVj+BQ4q8Lz7AyX+EVkyiTKwuJwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPDhMWDBUFGgIQFR4cCg8CGBcAEAAQGwAKABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAQEAAQEBAgICAgICAgICAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP///////////////////////////////////yog8P//////AACg/////z8AAAD6////DwAAAPD///8PAAAA8P///w8AAADg////DwAAAOD///8PAAAAwP///w8AAADA////DwAAAMD///8KAAAAwP///woAAADA////CgAAAMD///8KAAAAwP///woAAADA////CgAAAMD///8PAAAAwP///w8AAADA////DwAAAMD///8PAAAAwP///w8AAADA////DwAAAOD///8/AAAK8P////8PAAD6//////////////////////////////////////////////////////8AAIx0ARgYAAAAAAIAAAAAAAAAAO6nzdznuC3oltgABBT6BvRB58/jSAD3IDYrEeMnYh1kAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKKthezyCWnYfyWW9QoSe1DI1IwUnW9WRTdn9/AQiWv8nqGO3DjyYYWRmLakyp1IHbLAs+r4fc31KFFYQBkfms7OtbP6+CIzzgQ538B3ZyP11NO8ATl3Bm60L2hdHsypQR0pDhF+Dqx50tJoSAaGmdgcWutD98CaqphlXyLk+7haxbIwaKCNkINPeHZ2jSEQVyI2XWUslaR2oXuwhgJx438bjvx3S4hS9NyFILTqXjxzkPbcN416LZ+6kb+YDoW92tv10Hujhl64uu8CFK4oOVqNgUY6MOrRcLGxHfpocSFAb9XekZZCDTHk1Q11pkzA+CeocETO6zmnHSRDyHY3sxcjh1wvpnCFL0MzZje55fqneXphOkrPWKVt+sS7U8n03MkXHwIC8m0UPkctGXqg1762zJDMa9J/KpQsaDBGQNwb7d40tUjYtPQ3lJe6TqA9bxsdMVdzlLS4ST+auw22E+f0z3TTx9jEo6SruiYAlJL4Djn+rDKKse3KcVUeCnFVpl0uB9lW5cfk5ZdK3RdtH6h3UEXogySwdVrCS+zwZEqNiFpvGrlqxZ13pnOX5S81IBg2qa2vnF+KhMdUt5dRB//d1Ih4SNbBqdG7wnXPfy6KOT4tVT3aGejM1rlM4Lf1R3qf2VWcP3UVFjNcbH+zxb9Nz4Akxfj0/N11i1nby7y6tM83L618/e0n/LgjrLfnwIkDLhl0TYptFwPjd+7PJOe0WwQYcASZjE887Jc1ypVyukwMI1/xaZ2Zmgm6de21jrs+y+pn6aDsH7yQq4xqcl4jMCVSnNnk0LOMfkc=";
        byte[] bFpData1 = Base64StringFormBytes(sFpData1);
        Pointer bBuf1 = new Memory(bFpData1.length);
        bBuf1.write(0, bFpData1, 0, bFpData1.length);
        if (!CheckFpData(bBuf1)) {
            return;
        }

        byte[] bFpData2 = Base64StringFormBytes(sFpData2);
        Pointer bBuf2 = new Memory(bFpData2.length);
        bBuf2.write(0, bFpData2, 0, bFpData2.length);
        if (!CheckFpData(bBuf2)) {
            return;
        }
        //3、将需要对比指纹的数据添加到缓冲区
        int iRet = ast2600N.pisAddTptArray(iContextId, 1, bBuf1);
        if (iRet == PIS_FP_OK) {
            System.out.println("加入到缓冲区成功！");

        } else {
            System.out.println("加入到缓冲区失败！");
            return;
        }
        IntByReference identifiedID = new IntByReference();
        iRet = ast2600N.pisIdentifyTpl(iContextId, bBuf2, identifiedID);
        if (iRet == PIS_FP_OK) {
        System.out.println("对比成功"+identifiedID.getValue());
        }else {
            System.out.println("对比失败，两次指纹不一致");
        }

    }

    /**
     * 校验指纹数据
     */
    public static boolean CheckFpData(Pointer bBuf) {
        //1、校验数据是否正确

        int iRet = fpDataConv.FPCONV_GetFpDataValidity(bBuf);
        if (iRet != PIS_FP_OK) {
            System.out.println("指纹校验失败！");
            return false;
        }
        //2、先校验指纹数据版本
        IntByReference ifpVer = new IntByReference();
        IntByReference iFpSize = new IntByReference();
        iRet = fpDataConv.FPCONV_GetFpDataVersionAndSize(bBuf, ifpVer, iFpSize);
        if (iRet != PIS_FP_OK) {
            System.out.println("获取指纹信息失败！");
            return false;
        }
        if (ifpVer.getValue() != BioMiniLibrary.FpDataConv.DATA_VER_85) {
            System.out.println("指纹版本不正确，需要Ver85版本！");
            return false;
        }
        return true;

    }

    /**
     * 转base64字符串
     *
     * @param data
     */
    public static void printBase64String(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        final String encodedText = encoder.encode(data);
        System.out.println(encodedText);
    }


    /**
     * base64字符串转数组
     *
     * @param sBase64Str
     * @return
     */
    public static byte[] Base64StringFormBytes(String sBase64Str) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] value = null;
        try {
            value = decoder.decodeBuffer(sBase64Str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
