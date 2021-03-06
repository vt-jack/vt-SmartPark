package com.snk.door.mapper;

import com.snk.door.domain.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备信息Mapper接口
 * 
 * @author snk
 * @date 2020-08-03
 */
public interface DeviceMapper
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
    Device selectDeviceByControlPort(@Param("sn") String sn, @Param("controlPort") Short controlPort);

    /**
     * 查询设备信息列表
     *
     * @param device 设备信息
     * @return 设备信息集合
     */
    List<Device> selectDeviceList(Device device);

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
     * 删除设备信息
     * 
     * @param id 设备信息ID
     * @return 结果
     */
    int deleteDeviceById(Long id);

    /**
     * 批量删除设备信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDeviceByIds(String[] ids);

}
