package com.snk.door.service;

import com.snk.door.domain.Device;

import java.util.List;

/**
 * 设备信息Service接口
 * 
 * @author snk
 * @date 2020-08-03
 */
public interface IDeviceService
{
    /**
     * 查询设备信息
     * 
     * @param id 设备信息ID
     * @return 设备信息
     */
    Device selectDeviceById(Long id);

    /**
     * 查询设备信息
     *
     * @param sn SN
     * @return 设备信息
     */
    List<Device> selectDeviceBySn(String sn);

    /**
     * 查询设备信息
     *
     * @param sn SN
     * @param controlPort 控制端口
     * @return 设备信息
     */
    Device selectDeviceByControlPort(String sn, Short controlPort);

    /**
     * 查询设备信息列表
     * 
     * @param device 设备信息
     * @return 设备信息集合
     */
    List<Device> selectDeviceList(Device device);

    /**
     * 初始化设备信息
     *
     * @param device 设备
     * @return
     */
    void initDevice(Device device);

    /**
     * 查询所有设备信息
     * @return
     */
    List<Device> selectDeviceAll();

    /**
     * 新增设备信息
     * 
     * @param device 设备信息
     * @return 结果
     */
    int insertDevice(Device device);

    /**
     * 批量新增设备信息
     *
     * @param deviceList 设备信息
     * @return 结果
     */
    int insertDeviceBatch(List<Device> deviceList);

    /**
     * 修改设备信息
     * 
     * @param device 设备信息
     * @return 结果
     */
    int updateDevice(Device device);

    /**
     * 修改设备密码
     *
     * @param device 设备信息
     * @return
     */
    int updateDeviceBySn(Device device);

    /**
     * 批量删除设备信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDeviceByIds(String ids);

    /**
     * 删除设备信息信息
     * 
     * @param id 设备信息ID
     * @return 结果
     */
    int deleteDeviceById(Long id);

    /**
     * 校验唯一
     * @param device
     * @return
     */
    String checkUnique(Device device);

}
