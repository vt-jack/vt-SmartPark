package com.snk.door.mapper;

import com.snk.door.domain.Manager;

import java.util.List;

/**
 * 设备权限管理 数据层
 * 
 * @author snk
 */
public interface DeviceManagerMapper
{

    /**
     * 查询设备权限管理数据
     * 
     * @param manager 权限信息
     * @return 权限信息集合
     */
    List<Manager> selectManagerList(Manager manager);

    /**
     * 新增设备权限信息
     * 
     * @param manager 权限信息
     * @return 结果
     */
    int insertManager(Manager manager);

    /**
     * 批量新增设备权限信息
     *
     * @param managerList 权限信息
     * @return 结果
     */
    int insertManagerBatch(List<Manager> managerList);

    /**
     * 根据权限ID查询信息
     * 
     * @param id 权限ID
     * @return 权限信息
     */
    Manager selectManagerById(Long id);

    /**
     * 批量删除设备权限信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteManagerByIds(String[] ids);

    /**
     * 批量删除开门密码信息
     *
     * @param deviceIds 设备ID
     * @return 结果
     */
    int deleteManagerByDeviceIds(String[] deviceIds);

}
