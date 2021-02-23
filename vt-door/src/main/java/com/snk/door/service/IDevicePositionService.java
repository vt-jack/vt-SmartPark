package com.snk.door.service;

import com.snk.common.core.domain.Ztree;
import com.snk.door.domain.Position;

import java.util.List;

/**
 * 设备位置管理 服务层
 * 
 * @author snk
 */
public interface IDevicePositionService
{
    /**
     * 查询设备位置管理数据
     * 
     * @param position 部门信息
     * @return 部门信息集合
     */
    List<Position> selectPositionList(Position position, Boolean isDefault);

    /**
     * 查询设备位置管理树
     * 
     * @param position 位置信息
     * @return 所有位置信息
     */
    List<Ztree> selectPositionTree(Position position);

    /**
     * 查询设备位置管理树（排除下级）
     * 
     * @param position 位置信息
     * @return 所有部位置信息
     */
    List<Ztree> selectPositionTreeExcludeChild(Position position);

    /**
     * 查询位置数
     * 
     * @param parentId 父位置ID
     * @return 结果
     */
    int selectPositionCount(Long parentId);

    /**
     * 查询位置是否存在设备
     *
     * @param id 位置ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkPositionExistDevice(Long id);

    /**
     * 删除位置管理信息
     * 
     * @param id 位置ID
     * @return 结果
     */
    int deletePositionById(Long id);

    /**
     * 新增保存位置信息
     * 
     * @param position 位置信息
     * @return 结果
     */
    int insertPosition(Position position);

    /**
     * 修改保存位置信息
     * 
     * @param position 位置信息
     * @return 结果
     */
    public int updatePosition(Position position);

    /**
     * 根据位置ID查询信息
     * 
     * @param id 位置ID
     * @return 位置信息
     */
    Position selectPositionById(Long id);

    /**
     * 校验位置名称是否唯一
     * 
     * @param position 部门信息
     * @return 结果
     */
    String checkPositionNameUnique(Position position);
}
