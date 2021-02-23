package zkteco.id100com.jni;

import com.snk.common.config.Global;

public class id100sdk {
	/**
	 * <code>id100sdk</code> 身份证阅读器接口类
	 */
	static
	{
		System.load(Global.getProfile() + "/zk/id_card/termb.dll");
	}

	/**
	 * 获取文件目录
	 * @return 文件目录
	 */
	public native static String getPath();

	/**
	 * 连接身份证阅读器
	 *
	 * @param nPort 设备端口（串口:1~16;USB:1001~1016）
	 * @return
	 * 成功返回设备端口
	 * 失败返回0
	 */
	public native static int InitComm(int nPort);

	/**
	 * 自动搜索并连接身份证阅读器
	 *
	 * @return
	 * 成功返回设备端口（串口:1~16;USB:1001~1016）
	 * 失败返回0
	 */
	public native static int InitCommExt();


	/**
	 * 	断开与身份证阅读器连接
	 * @return
	 * 成功返回1
	 * 失败返回0
	 */
	public native static int CloseComm();

	/**
	 * 认证卡(寻卡&选卡)
	 * @return
	 * 成功返回1
	 * 失败返回0
	 */
	public native static int Authenticate();

	/**
	 * 读卡操作，信息文件保存在dll所在目录
	 * @param nActive	1读基本信息(文字&照片&指纹);2读文字照片;3读最新地址
	 * @return	1 居民身份证读取成功;2 外国人永居证读取成功;3 港澳台居住证读取成功;-11无效参数;0其他错误
	 */
	public native static int ReadContent(int nActive);

	/**
	 * 获取SAM模块编号
	 * @return	成功返回SAM模块编号;失败返回空字符串
	 */
	public native static String GetSAMID();

	/**
	 * 设置临时文件生成的目录;空目录，则不保存临时文件
	 */
	public native static void SetTemDir(String FileName);

	/**
	 * 获取SAM模块编号(10位编号)
	 * @return	成功返回SAM模块编号;失败返回空字符串
	 */
	public native static String GetSAMIDEx();

	/**
	 * 解析(xp.wlt)身份证照片
	 * @param FileName xp.wlt全路径名
	 * @return	成功返回1;失败返回0
	 */
	public native static int GetBmpPhoto(String FileName);


	/**
	 * 解析(dll目录下的xp.wlt)身份证照片
	 * @return	成功返回1;失败返回0
	 */
	public native static int GetBmpPhotoExt();

	/**
	 * 重置sam模块
	 * @return	成功返回1;失败返回0
	 */
	public native static int ResetSAM();


	/**
	 * 获取SAM模块状态
	 * @return	状态正常返回1;异常返回0
	 */
	public native static int GetSAMStatus();

	/**
	 * 获取姓名
	 * @return 返回姓名
	 */
	public native static String getName();


	/**
	 * 获取性别
	 * @return 返回性别
	 */
	public native static String getSex();

	/**
	 * 获取性别编号
	 * @return	返回性别编号
	 */
	public native static int getSexCode();

	/**
	 * 获取民族
	 * @return	返回民族
	 */
	public native static String getNation();


	/**
	 * 获取民族编号
	 * @return	返回毛南族编号
	 */
	public native static int getNationCode();

	/**
	 * 获取生日(YYYYMMDD)
	 * @return	返回生日
	 */
	public native static String getBirthdate();

	/**
	 * 获取地址
	 * @return  返回地址
	 */
	public native static String getAddress();


	/**
	 * 获取身份证号码
	 * @return	返回身份证号码
	 */
	public native static String getIDNum();


	/**
	 * 获取签发机关
	 * @return	返回签发机关
	 */
	public native static String getIssue();

	/**
	 * 获取有效期起始日期(YYYYMMDD)
	 * @return	返回有效期起始日期
	 */
	public native static String getEffectedDate();


	/**
	 * 获取有效期截止日期
	 * @return	返回有效期截止日期
	 */
	public native static String getExpireDate();

	/*
	 * 获取bmp图片base64编码
	 * @return 返回bmp图片base64编码
	 */
	public native static String getBMPPhotoBase64();


	/**
	 * 获取JPG头像Base64编码
	 * @return 返回JPG头像Base64编码
	 */
	public native static String getJPGPhotoBase64();



	/**
	 * 语音提示
	 * @param nVoice 语音类型
	 * 0:
	 * @return	成功返回1;失败返回0
	 */
	public native static int HIDVoice(int nVoice);

	/**
	 * 设置发卡器序列号
	 * @param iPort	串口号
	 * @param data	发卡器序列号
	 * @return	成功返回1;失败返回0
	 */
	public native static int ICSetDevNum(int iPort, String data);

	/**
	 * 获取发卡器序列号
	 * @param iPort	串口号
	 * @return	成功返回发卡器序列号;失败返回空字符串
	 */
	public native static String ICGetDevNum(int iPort);

	/**
	 * 获取发卡器版本
	 * @param iPort	串口号
	 * @return 成功返回发卡器版本（HexString），失败返回空字符串
	 */
	public native static String ICGetDevVersion(int iPort);

	/**
	 * 发卡
	 * @param iPort	串口号
	 * @param keyMode	keyA/KeyB
	 * @param Sector	扇区号
	 * @param idx	//块索引
	 * @param key	//密钥
	 * @param data	//数据
	 * @param snr	//返回卡号
	 * @return	成功返回1;失败返回0
	 */
	public native static int ICWriteData(int iPort, int keyMode, int Sector, int idx, String key, String data, int[] snr);

	/**
	 * 读卡
	 * @param iPort	串口号
	 * @param keyMode	keyA/KeyB
	 * @param Sector	扇区号
	 * @param idx	//块索引
	 * @param key	//密钥
	 * @param snr	//返回卡号
	 * @return 成功返回卡数据（HexString）；失败返回空串
	 */
	public native static String ICReadData(int iPort, int keyMode, int Sector, int idx, String key, int[] snr);


	/**
	 * 读取IC卡物理卡号
	 * @param iPort	串口号
	 * @param snr	返回IC卡物理卡号
	 * @return	成功返回1;失败返回0
	 */
	public native static int ICGetICSnr(int iPort, int[] snr);


	/**
	 * 获取ID卡物理卡号
	 * @param iPort	串口号
	 * @return	成功返回ID卡物理卡号(HexString)；失败返回空串
	 */
	public native static String ICGetIDSnr(int iPort);

	/**
	 * 	修改扇区密码
	 * @param iPort 串口号
	 * @param keyMode	keyA/KeyB
	 * @param Sector	扇区号(0~15)
	 * @param oldKey	keyA/KeyB原始密码
	 * @param newKey	新密码
	 * @return	成功返回1;失败返回0
	 */
	public native static int ICChangeSectorPassword(int iPort, int keyMode, int Sector, String oldKey, String newKey);

	/**
	 * 设置身份证头像透明阀值（RGB）,3值均大于设置值透明
	 * @param R	Red
	 * @param g green
	 * @param b blue
	 */
	public native static void SetTransparentThreshold(byte R, byte g, byte b);

	/**
	 * 获取身份证指纹模板
	 * @return 返回横版身份证指纹模板Base64编码
	 */
	public native static String getFPDataBase64();

	/**
	 * 获取证件类型
	 * @return	返回证件类型（1 二代证; 2 外国人永居证，3 港澳台居住证）
	 */
	public native static int getCardType();

	/**
	 * 获取银行卡卡号
	 * @param iPort	串口号
	 * @return	成功返回ID卡银行卡卡号；失败返回空串
	 */
	public native static String ICGetBankCardNum(int iPort);

	/**
	 * 获取港澳台居住证签证次数
	 * @return	签证次数
	 */
	public native static int getVisaTimes();

	/**
	 * 获取港澳台居住证通行证号
	 * @return 返回港澳台居住证通行证号
	 */
	public native static String getPassNum();

	/**
	 * 获取外国人英文名
	 * @return 返回外国人英文名
	 */
	public native static String getEnName();

	/**
	 * 获取外国人中文名
	 * @return 返回外国人中文名
	 */
	public native static String getCnName();
}
