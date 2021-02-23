package com.snk.door.service.impl;

import com.snk.common.core.text.Convert;
import com.snk.door.domain.Manager;
import com.snk.door.mapper.DeviceManagerMapper;
import com.snk.door.service.IDeviceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备权限管理 服务实现
 * 
 * @author snk
 */
@Service
public class DeviceManagerServiceImpl implements IDeviceManagerService
{
    @Autowired
    private DeviceManagerMapper deviceManagerMapper;

    /**
     * 查询设备设备权限管理数据
     *
     * @param manager 设备权限信息
     * @return 设备权限信息集合
     */
    @Override
    public List<Manager> selectManagerList(Manager manager)
    {
        return deviceManagerMapper.selectManagerList(manager);
    }

    /**
     * 新增保存设备权限信息
     *
     * @param manager 设备权限信息
     * @return 结果
     */
    @Override
    public int insertManager(Manager manager)
    {
        return deviceManagerMapper.insertManager(manager);
    }

    /**
     * 批量新增保存设备权限信息
     *
     * @param managerList 设备权限信息
     * @return 结果
     */
    @Override
    public int insertManagerBatch(List<Manager> managerList) {
        return deviceManagerMapper.insertManagerBatch(managerList);
    }

    /**
     * 根据设备权限ID查询信息
     *
     * @param id 设备权限ID
     * @return 设备权限信息
     */
    @Override
    public Manager selectManagerById(Long id)
    {
        return deviceManagerMapper.selectManagerById(id);
    }

    /**
     * 根据设备权限ID查询信息
     *
     * @param ids 设备权限ID集合
     * @return 设备权限信息
     */
    @Override
    public int deleteManagerByIds(String ids)
    {
        return deviceManagerMapper.deleteManagerByIds(Convert.toStrArray(ids));
    }

    /**
     * 批量删除设备权限信息
     *
     * @param deviceIds 需要删除的设备ID
     * @return 结果
     */
    @Override
    public int deleteManagerByDeviceIds(String deviceIds) {
        return deviceManagerMapper.deleteManagerByDeviceIds(Convert.toStrArray(deviceIds));
    }

}
