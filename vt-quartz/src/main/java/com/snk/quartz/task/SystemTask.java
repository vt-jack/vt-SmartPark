package com.snk.quartz.task;

import com.snk.common.constant.SymbolConstants;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.control.read.*;
import com.snk.door.api.face.ReadOEMService;
import com.snk.door.api.face.ReadPersonDataBaseService;
import com.snk.door.api.face.ReadPersonDatabaseDetailService;
import com.snk.door.domain.Blacklist;
import com.snk.door.domain.CwaRecord;
import com.snk.door.domain.Device;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.WorkState;
import com.snk.door.service.IBlacklistService;
import com.snk.door.service.ICwaRecordService;
import com.snk.door.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时任务调度
 * 
 * @author snk
 */
@Component("systemTask")
public class SystemTask
{
    private static final Logger log = LoggerFactory.getLogger(SystemTask.class);

    @Autowired
    private IBlacklistService blacklistService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private ReadVersionService readVersionService;
    @Autowired
    private ReadConnectPasswordService readConnectPasswordService;
    @Autowired
    private ReadTCPSettingService readTCPSettingService;
    @Autowired
    private ReadDeadlineService readDeadlineService;
    @Autowired
    private ReadRelayReleaseTimeService readRelayReleaseTimeService;
    @Autowired
    private ReadCardDataBaseService readCardDataBaseService;
    @Autowired
    private ReadRecordModeService readRecordModeService;
    @Autowired
    private ReadPushButtonSettingService readPushButtonSettingService;
    @Autowired
    private ReadSystemStatusService readSystemStatusService;
    @Autowired
    private ReadAntiPassbackService readAntiPassbackService;
    @Autowired
    private ReadCardDatabaseDetailService readCardDatabaseDetailService;
    @Autowired
    private ReadTransactionDatabaseDetailService readTransactionDatabaseDetailService;
    @Autowired
    private ReadTimeGroupService readTimeGroupService;
    @Autowired
    private ReadTransactionDatabaseService readTransactionDatabaseService;
    @Autowired
    private ReadPersonDatabaseDetailService readPersonDatabaseDetailService;
    @Autowired
    private ReadPersonDataBaseService readPersonDataBaseService;
    @Autowired
    private ReadOEMService readOEMService;
    @Autowired
    private ReadSNService readSNService;
    @Autowired
    private ICwaRecordService cwaRecordService;

    /**
     * 黑名单解禁
     */
    public void blacklist()
    {
        log.info("定时任务开始执行：blacklist，时间：{}", DateUtils.getNowDate());
        List<Blacklist> blackList = blacklistService.selectLiftList();
        StringBuffer sb = new StringBuffer();
        blackList.stream().forEach(item -> {
            sb.append(SymbolConstants.COMMA).append(item.getId());
        });
        if (StringUtils.isNotEmpty(sb)) {
            log.info("解禁黑名单ID如下：{}", sb.substring(1));
            blacklistService.deleteBlacklistByIds(sb.substring(1));
        }
        log.info("定时任务执行完成：blacklist，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集版本号
     */
    public void readVer(String sn) {
        log.info("定时任务开始执行：readVersion，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readVersionService.readVersion(device));
        log.info("定时任务执行完成：readVersion，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集工作状态
     */
    public void readState(String sn) {
        log.info("定时任务开始执行：readState，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, false).stream().forEach(device -> readSNService.readSn(device));
        log.info("定时任务执行完成：readState，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集pwd
     */
    public void readPwd(String sn) {
        log.info("定时任务开始执行：readPwd，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readConnectPasswordService.readConnectPassword(device));
        log.info("定时任务执行完成：readPwd，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集Tcp网络参数
     */
    public void readTcp(String sn) {
        log.info("定时任务开始执行：readTcp，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readTCPSettingService.readTCPSetting(device));
        log.info("定时任务执行完成：readTcp，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集设备有效期
     */
    public void readDeadline(String sn) {
        log.info("定时任务开始执行：readDeadline，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,false, true).stream().filter(device -> DeviceType.CONTROL.getValue().equals(device.getDeviceType()))
                .forEach(device -> readDeadlineService.readDeadline(device));
        log.info("定时任务执行完成：readDeadline，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集开门保持时长
     */
    public void readReleaseTime(String sn) {
        log.info("定时任务开始执行：readReleaseTime，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,false, true).stream().forEach(device -> readRelayReleaseTimeService.readRelayReleaseTime(device));
        log.info("定时任务执行完成：readReleaseTime，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集卡片数据
     */
    public void readCardData(String sn) {
        log.info("定时任务开始执行：readCardType，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readCardDataBaseService.readCardDataBase(device));
        log.info("定时任务执行完成：readCardType，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集存储方式
     */
    public void readRecordMode(String sn) {
        log.info("定时任务开始执行：readRecordMode，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readRecordModeService.readRecordMode(device));
        log.info("定时任务执行完成：readRecordMode，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集出门按钮设置
     */
    public void readPushButton(String sn) {
        log.info("定时任务开始执行：readPushButton，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,false, true).stream().filter(device -> DeviceType.CONTROL.getValue().equals(device.getDeviceType())).forEach(device -> readPushButtonSettingService.readPushButtonSetting(device));
        log.info("定时任务执行完成：readPushButton，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集设备运行信息
     */
    public void readSystemStatus(String sn) {
        log.info("定时任务开始执行：readSystemStatus，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readSystemStatusService.readSystemStatus(device));
        log.info("定时任务执行完成：readSystemStatus，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集防潜回设置
     */
    public void readAntiBack(String sn) {
        log.info("定时任务开始执行：readAntiBack，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,false, true).stream().filter(device -> DeviceType.CONTROL.getValue().equals(device.getDeviceType())).forEach(device -> readAntiPassbackService.readAntiPassback(device));
        log.info("定时任务执行完成：readAntiBack，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集卡信息
     */
    public void readCardInfo(String sn) {
        log.info("定时任务开始执行：readCardData，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().filter(device -> DeviceType.CONTROL.getValue().equals(device.getDeviceType())).forEach(device -> readCardDatabaseDetailService.readCardDatabaseDetail(device));
        log.info("定时任务执行完成：readCardData，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集控制器数据信息
     */
    public void readTransactionData(String sn) {
        log.info("定时任务开始执行：readTransactionData，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readTransactionDatabaseDetailService.readTransactionDatabaseDetail(device));
        log.info("定时任务执行完成：readTransactionData，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集开门时间段
     */
    public void readTimeGroup(String sn) {
        log.info("定时任务开始执行：readTimeGroup，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readTimeGroupService.readTimeGroup(device));
        log.info("定时任务执行完成：readTimeGroup，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集设备记录
     */
    public void readRecord(String sn) {
        log.info("定时任务开始执行：readRecord，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().forEach(device -> readTransactionDatabaseService.readTransactionDatabase(device));
        log.info("定时任务执行完成：readRecord，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集人员数据
     */
    public void readPersonDetail(String sn) {
        log.info("定时任务开始执行：readPersonDetail，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().filter(device -> !DeviceType.CONTROL.getValue().equals(device.getDeviceType())).forEach(device -> readPersonDatabaseDetailService.readPersonDatabaseDetail(device));
        log.info("定时任务执行完成：readPersonDetail，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集人员信息
     */
    public void readPerson(String sn) {
        log.info("定时任务开始执行：readPerson，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().filter(device -> !DeviceType.CONTROL.getValue().equals(device.getDeviceType())).forEach(device -> readPersonDataBaseService.readPersonDatabase(device));
        log.info("定时任务执行完成：readPerson，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 采集OEM
     */
    public void readOEM(String sn) {
        log.info("定时任务开始执行：readOEM，时间：{}", DateUtils.getNowDate());
        getDeviceList(sn,true, true).stream().filter(device -> !DeviceType.CONTROL.getValue().equals(device.getDeviceType())).forEach(device -> readOEMService.readOEM(device));
        log.info("定时任务执行完成：readOEM，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 考勤报表统计
     */
    public void cwaRecord() {
        log.info("定时任务开始执行：cwaRecord，时间：{}", DateUtils.getNowDate());
        cwaRecordService.initCwaRecord(new CwaRecord());
        log.info("定时任务执行完成：cwaRecord，时间：{}", DateUtils.getNowDate());
    }

    /**
     * 获取设备列表
     * @param sn SN
     * @param iterate 是否根据sn去重
     * @param line 是否在线
     * @return
     */
    private List<Device> getDeviceList(String sn, boolean iterate, boolean line) {
        List<Device> deviceList = line ? deviceService.selectDeviceAll().stream().filter(item ->
                WorkState.ON_LINE.getValue().equals(item.getWorkState())).collect(Collectors.toList()) : deviceService.selectDeviceAll();
        List<Device> result;
        if (StringUtils.isNotEmpty(sn)) {
            List<String> snList = Arrays.asList(sn.split(SymbolConstants.COMMA));
            result = deviceList.stream().filter(item -> snList.contains(item.getSn())).collect(Collectors.toList());
        } else {
            result = deviceList;
        }
        if (iterate) {
            return result.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Device::getSn))), ArrayList::new));
        }
        return result;
    }
}
