package com.snk.door.api.finger.yz;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * 指纹仪库
 */
public class BioMiniLibrary {
    public interface Ast2600N extends Library {
        /**
         * 指纹仪操作类
         * 将Ast2600N.dll放置到项目的根目录下即可
         */
        Ast2600N instance = Native.load("Ast2600N", Ast2600N.class);

        /**
         * 获取一个指纹图像输入设备
         *
         * @param devOrderNumber 序号
         * @param devId          设备号
         * @param devName        设备名称
         * @return 执行成功返回 0
         */
        int pisEnumerateDevice(int devOrderNumber, byte[] devId, byte[] devName);

        /**
         * 创建一个上下文
         *
         * @param contextId 上下文ID
         * @return 执行成功返回 0
         */
        int pisCreateContext(IntByReference contextId);

        /**
         * 销毁上下文
         *
         * @param contextId 上下文ID
         * @return 执行成功返回 0
         */
        int pisDestroyContext(int contextId);

        /**
         * 打开指纹设备连接
         *
         * @param contextId 上下文ID
         * @param devId     设备Id
         * @return 执行成功返回 0
         */
        int pisOpenDevice(int contextId, byte[] devId);

        /**
         * 关闭指纹设备连接
         *
         * @param contextId 上下文Id
         * @return 执行成功返回 0
         */
        int pisCloseDevice(int contextId);

        /**
         * 获取设备相关信息
         *
         * @param contextId    上下文ID
         * @param engineInfo   算法引擎
         * @param imaWidth     图像宽度
         * @param imaHeight    图像高度
         * @param imaRes       图像扫描精度
         * @param featureSize  临时模板尺寸
         * @param templateSize 正式模板尺寸
         * @return 执行成功返回 0
         */
        int pisGetInfo(int contextId, byte[] engineInfo, IntByReference imaWidth, IntByReference imaHeight, IntByReference imaRes, IntByReference featureSize, IntByReference templateSize);

        /**
         * 语音播报<br>
         * 语音类型如下：<br>
         * 0.请按指纹<br>
         * 1.请重新按指纹<br>
         * 2.请再次按指纹<br>
         * 3.登记成功<br>
         * 4.谢谢<br>
         * 5.该指纹已有注册<br>
         * 6.请按上一个指纹<br>
         * 7.请读卡<br>
         *
         * @param contextId 上下文ID
         * @param index     语音类型
         * @return 执行成功返回 0
         */
        int pisSoundPlay(int contextId, int index);

        /**
         * 获得活体指纹图像输入设备的一个图像
         *
         * @param contextId 上下文ID
         * @param imgBuffer 图片数据
         * @return 执行成功返回 0
         */
        int pisCapture(int contextId, Pointer imgBuffer);

        /**
         * 是否按下指纹
         *
         * @param contextId 上下文ID
         * @param imgBuffer 图片数据
         * @param imaWidth  图片宽度
         * @param imaHeight 图片高度
         * @param imaRes    图像扫描精度
         * @param isCheckFp 是否按下指纹大于0表示有指纹
         * @param fpArea    图片清晰度
         * @return 执行成功返回 0
         */
        int pisCheckFp(int contextId, Pointer imgBuffer, int imaWidth, int imaHeight, int imaRes, IntByReference isCheckFp, IntByReference fpArea);

        /**
         * 加工指纹图片
         *
         * @param contextId   上下文ID
         * @param imgBuffer   图片数据
         * @param imaWidth    图片宽度
         * @param imaHeight   图片高度
         * @param imaRes      图像扫描精度
         * @param featureData 加工完成指纹数据
         * @return 执行成功返回 0
         */
        int pisProcess(int contextId, Pointer imgBuffer, int imaWidth, int imaHeight, int imaRes, Pointer featureData);

        /**
         * 创建正式特征码
         *
         * @param contextId    上下文ID
         * @param featureData1 指纹数据1
         * @param featureData2 指纹数据2
         * @param featureData3 指纹数据3
         * @param templateData 正式特征码
         * @return 执行成功返回 0
         */
        int pisCreateTemplate(int contextId, Pointer featureData1, Pointer featureData2, Pointer featureData3, Pointer templateData);

        /**
         *开始读卡/停止读卡
         * @param contextId 上下文ID
         * @param isOpen 1 - 开始，0 - 停止
         * @return 执行成功返回 0
         */
        int pisScanCard(int contextId,int isOpen);

        /**
         *读取卡号
         * @param contextId 上下文ID
         * @param cardNumber 卡号
         * @return 执行成功返回 0
         */
        int pisGetCardNumber(int contextId ,byte[] cardNumber);

        /**
         * 将数据加入到缓存区
         * @param contextId 上下文ID
         * @param templateID 模板号（需要对比的指纹数据必须模板号一致）
         * @param templateData 待添加的数据
         * @return 执行成功返回 0
         */
        int pisAddTptArray(int contextId,int templateID,Pointer templateData );

        /**
         * 对比缓冲区指纹
         * @param contextId
         * @param queryTemplateData
         * @param identifiedID
         * @return
         */
        int pisIdentifyTpl(int contextId ,Pointer queryTemplateData ,IntByReference identifiedID);

    }

    public interface RC4 extends Library {
        RC4 instance = Native.load("RC4", RC4.class);

        /**
         * 保存图片至本地
         *
         * @param imgBuffer 图片数据
         * @param imaWidth  图片宽度
         * @param imaHeight 图片高度
         * @param bFilePath 图片保存地址
         * @return 执行成功返回 1
         */
        int SaveGrayBitmapToFile(Pointer imgBuffer, int imaWidth, int imaHeight, Pointer bFilePath);
    }

    public interface FpDataConv extends Library {
        FpDataConv instance = Native.load("FpDataConv", FpDataConv.class);
        int DATA_VER_70 = 0x70;
        int DATA_VER_80 = 0x80;
        int DATA_VER_85 = 0x85;
        int DATA_VER_89 = 0x89;

        /**
         * 获取动态库版本号
         *
         * @param strModel 版本号
         * @return 执行成功返回 0
         */
        int FPCONV_GetConvDLLModel(byte[] strModel);

        /**
         * 检测指纹数据的有效性
         *
         * @param bBuf 指纹数据
         * @return 执行成功返回 0
         */
        int FPCONV_GetFpDataValidity(Pointer bBuf);

        /**
         * 获取指纹数据的算法版本号以及缓冲区尺寸
         *
         * @param bBuf       指纹数据
         * @param apnVersion 算法版本号
         * @param apnSize    缓冲区尺寸
         * @return 执行成功返回 0
         */
        int FPCONV_GetFpDataVersionAndSize(Pointer bBuf, IntByReference apnVersion, IntByReference apnSize);

        /**
         * 获取版本对应缓冲区大小
         * @param iFpDataVersion 版本号
         * @param apnDataSize 缓冲区大小
         * @return 执行成功返回 0
         */
        int FPCONV_GetFpDataSize(int iFpDataVersion,IntByReference apnDataSize);

        /**
         * 转换指纹数据版本
         * @param iSrcVer 原版本号
         * @param bSrcFpData 原数据
         * @param iDestVer 转换的版本号
         * @param bDestFpData 转换的数据
         * @return 执行成功返回 0
         */
        int FPCONV_Convert(int iSrcVer,Pointer bSrcFpData,int iDestVer,Pointer bDestFpData);
    }

    /**
     * 语音播放
     */
    public enum Sound {
        请按指纹,
        请重新按指纹,
        请再次按指纹,
        登记成功,
        谢谢,
        该指纹已有注册,
        请按上一个指纹,
        请读卡;
    }
}
