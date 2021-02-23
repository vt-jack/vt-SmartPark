package com.snk.door.service;

import com.snk.door.domain.Manager;
import com.snk.door.domain.Position;

import java.util.List;

/**
 * 设备设备权限管理 服务层
 * 
 * @author snk
 */
public interface IDeviceManagerService
{
    /**
     * 查询设备设备权限管理数据
     *
     * @param manager 设备权限信息
     * @return 设备权限信息集合
     */
    List<Manager> selectManagerList(Manager manager);

    /**
     * 新增设备设备权限信息
     *
     * @param manager 设备权限信息
     * @return 结果
     */
    int insertManager(Manager manager);

    /**
     * 批量设备设备权限信息
     *
     * @param managerList 设备权限信息
     * @return 结果
     */
    int insertManagerBatch(List<Manager> managerList);

    /**
     * 根据设备权限ID查询信息
     *
     * @param id 设备权限ID
     * @return 设备权限信息
     */
    Manager selectManagerById(Long id);

    /**
     * 批量删除设备权限信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteManagerByIds(String ids);

    /**
     * 批量删除设备权限信息
     *
     * @param deviceIds 需要删除的设备ID
     * @return 结果
     */
    int deleteManagerByDeviceIds(String deviceIds);

}
