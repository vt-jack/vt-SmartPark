package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.constant.UserConstants;
import com.snk.common.core.text.Convert;
import com.snk.common.utils.StringUtils;
import com.snk.common.utils.security.PermissionUtils;
import com.snk.door.api.control.read.*;
import com.snk.door.api.face.ReadOEMService;
import com.snk.door.api.face.ReadPersonDatabaseDetailService;
import com.snk.door.domain.Device;
import com.snk.door.domain.Manager;
import com.snk.door.enums.DeviceType;
import com.snk.door.mapper.DeviceMapper;
import com.snk.door.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备信息Service业务层处理
 * 
 * @author snk
 * @date 2020-08-03
 */
@Service
public class DeviceServiceImpl implements IDeviceService
{
    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ITimeSlotService timeSlotService;
    @Autowired
    private IDevicePwdService devicePwdService;
    @Autowired
    private IDeviceAuthService deviceAuthService;
    @Autowired
    private IDeviceManagerService deviceManagerService;
    @Autowired
    private ReadConnectPasswordService readConnectPasswordService;
    @Autowired
    private ReadTCPSettingService readTCPSettingService;
    @Autowired
    private ReadAntiPassbackService readAntiPassbackService;
    @Autowired
    private ReadCardDatabaseDetailService readCardDatabaseDetailService;
    @Autowired
    private ReadDeadlineService readDeadlineService;
    @Autowired
    private ReadPushButtonSettingService readPushButtonSettingService;
    @Autowired
    private ReadRecordModeService readRecordModeService;
    @Autowired
    private ReadRelayReleaseTimeService readRelayReleaseTimeService;
    @Autowired
    private ReadSystemStatusService readSystemStatusService;
    @Autowired
    private ReadTransactionDatabaseDetailService readTransactionDatabaseDetailService;
    @Autowired
    private ReadVersionService readVersionService;
    @Autowired
    private ReadPersonDatabaseDetailService readPersonDatabaseDetailService;
    @Autowired
    private ReadOEMService readOEMService;

    /**
     * 查询设备信息
     * 
     * @param id 设备信息ID
     * @return 设备信息
     */
    @Override
    public Device selectDeviceById(Long id)
    {
        return deviceMapper.selectDeviceById(id);
    }

    /**
     * 查询设备信息
     *
     * @param sn SN
     * @return 设备信息
     */
    @Override
    public List<Device> selectDeviceBySn(String sn)
    {
        return deviceMapper.selectDeviceBySn(sn);
    }

    /**
     * 查询设备信息
     *
     * @param sn SN
     * @param controlPort 控制端口
     * @return 设备信息
     */
    @Override
    public Device selectDeviceByControlPort(String sn, Short controlPort)
    {
        return deviceMapper.selectDeviceByControlPort(sn, controlPort);
    }

    /**
     * 查询设备信息列表
     * 
     * @param device 设备信息
     * @return 设备信息
     */
    @Override
    @DataScopeSnk(deviceAlias = "a")
    public List<Device> selectDeviceList(Device device)
    {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 初始化设备信息
     *
     * @param device 设备
     * @return
     */
    @Override
    public void initDevice(Device device) {
        // 清空权限数据
        deviceAuthService.deleteAuthBySn(device.getSn());
        // 清空开门密码
        devicePwdService.deletePwdBySn(device.getSn());
        // 清空开门时段
        timeSlotService.deleteTimeSlotBySn(device.getSn());
        // 采集密码
        readConnectPasswordService.readConnectPassword(device);
        // 采集版本号
        readVersionService.readVersion(device);
        // 采集TCP网络参数
        readTCPSettingService.readTCPSetting(device);
        // 采集控制器信息
        readTransactionDatabaseDetailService.readTransactionDatabaseDetail(device);
        // 采集存储方式
        readRecordModeService.readRecordMode(device);
        // 采集设备运行信息
        readSystemStatusService.readSystemStatus(device);
        // 采集开门保持时间
        readRelayReleaseTimeService.readRelayReleaseTime(device);
        if (DeviceType.CONTROL.getValue().equals(device.getDeviceType())) {
            // 采集有效期
            readDeadlineService.readDeadline(device);
            // 采集防潜回
            readAntiPassbackService.readAntiPassback(device);
            // 采集卡信息
            readCardDatabaseDetailService.readCardDatabaseDetail(device);
            // 采集出门按钮设置
            readPushButtonSettingService.readPushButtonSetting(device);
        } else {
            // 采集OEM
            readOEMService.readOEM(device);
            // 采集人员存储详情
            readPersonDatabaseDetailService.readPersonDatabaseDetail(device);
        }
    }

    /**
     * 查询所有设备信息
     *
     * @return 设备信息
     */
    @Override
    public List<Device> selectDeviceAll()
    {
        return deviceMapper.selectDeviceList(new Device());
    }

    /**
     * 新增设备信息
     * 
     * @param device 设备信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertDevice(Device device) {
        return deviceMapper.insertDevice(device);
    }

    /**
     * 批量新增设备信息
     *
     * @param deviceList 设备信息
     * @return 结果
     */
    @Override
    public int insertDeviceBatch(List<Device> deviceList) {
        int result = deviceMapper.insertDeviceBatch(deviceList);
        if (result > 0) {
            deviceList.stream().forEach(device -> initDevice(device));
        }
        timeSlotService.insertDefaultTimeSlot(deviceList.get(0));   // 初始化开门时段
        Boolean isSnkAdmin = (Boolean) PermissionUtils.getPrincipalProperty("snkAdmin");
        Long userId = (Long) PermissionUtils.getPrincipalProperty("userId");
        if (!isSnkAdmin) {  // 不是管理员需要分配权限
            List<Manager> managerList = new ArrayList<>();
            deviceList.stream().forEach(item -> {
                Manager manager = new Manager();
                manager.setUserId(userId);
                manager.setDeviceId(item.getId());
                manager.setCreateBy(item.getCreateBy());
                managerList.add(manager);
            });
            deviceManagerService.insertManagerBatch(managerList);
        }
        return result;
    }

    /**
     * 修改设备信息
     *
     * @param device 设备信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateDevice(Device device)
    {
        return deviceMapper.updateDevice(device);
    }

    /**
     * 修改设备密码
     *
     * @param device 设备信息
     * @return
     */
    @Transactional
    @Override
    public int updateDeviceBySn(Device device)
    {
        return deviceMapper.updateDeviceBySn(device);
    }

    /**
     * 删除设备信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDeviceByIds(String ids)
    {
        // 删除权限
        deviceManagerService.deleteManagerByDeviceIds(ids);
        // 删除开门权限
        deviceAuthService.deleteAuthByDeviceIds(ids);
        // 删除开门密码
        devicePwdService.deletePwdByDeviceIds(ids);
        // 查询设备信息
        String[] array = Convert.toStrArray(ids);
        Device queryParam = new Device();
        queryParam.setIds(array);
        Map<String, List<Device>> deviceMap = deviceMapper.selectDeviceList(queryParam).stream().collect(Collectors.groupingBy(Device::getSn)); // 根据SN分组
        for (String sn : deviceMap.keySet()) {
            List<Device> delList = deviceMap.get(sn);
            Device device = delList.get(0);
            List<Device> deviceList = this.selectDeviceBySn(sn);
            for (Device item : delList) {
                for (Device item1 : deviceList) {
                    if (item.getId().equals(item1.getId())) {
                        deviceList.remove(item1);
                        break;
                    }
                }
            }
            if (CollectionUtils.isEmpty(deviceList)) {   // 删除最后一条设备删除开门时段
                timeSlotService.deleteTimeSlotBySn(device.getSn());
                timeSlotService.deleteTimeSlotByIdx(device.getSn(), 1);
            }
        }
        return deviceMapper.deleteDeviceByIds(array);
    }

    /**
     * 删除设备信息信息
     *
     * @param id 设备信息ID
     * @return 结果
     */
    @Override
    public int deleteDeviceById(Long id)
    {
        return deviceMapper.deleteDeviceById(id);
    }

    /**
     * 校验唯一
     *
     * @param device 设备信息
     * @return
     */
    @Override
    public String checkUnique(Device device)
    {
        Long deviceId = StringUtils.isNull(device.getId()) ? -1L : device.getId();
        device.setDelFlag("0");
        List<Device> deviceList = deviceMapper.selectDeviceList(device);
        if (!CollectionUtils.isEmpty(deviceList) && deviceList.get(0).getId().longValue() != deviceId.longValue())
        {
            return UserConstants.EXCEPTION;
        }
        return UserConstants.NORMAL;
    }

}
