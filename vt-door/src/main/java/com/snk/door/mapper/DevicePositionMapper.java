package com.snk.door.mapper;

import com.snk.door.domain.Position;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备位置管理 数据层
 * 
 * @author snk
 */
public interface DevicePositionMapper
{
    /**
     * 查询位置数
     * 
     * @param position 位置信息
     * @return 结果
     */
    int selectPositionCount(Position position);

    /**
     * 查询位置是否存在设备
     *
     * @param id 位置ID
     * @return 结果 true 存在 false 不存在
     */
    int checkPositionExistDevice(Long id);

    /**
     * 查询位置是否存在用户
     *
     * @param id 位置ID
     * @return 结果
     */
    int checkPositionExistUser(Long id);

    /**
     * 查询设备位置管理数据
     * 
     * @param position 位置信息
     * @return 位置信息集合
     */
    List<Position> selectPositionList(Position position);

    /**
     * 查询设备位置管理数据（所有）
     *
     * @return 位置信息集合
     */
    List<Position> selectAllPosition();

    /**
     * 删除设备位置管理信息
     * 
     * @param id 位置ID
     * @return 结果
     */
    int deletePositionById(Long id);

    /**
     * 新增设备位置信息
     * 
     * @param position 位置信息
     * @return 结果
     */
    int insertPosition(Position position);

    /**
     * 修改设备位置信息
     * 
     * @param position 位置信息
     * @return 结果
     */
    int updatePosition(Position position);

    /**
     * 修改子元素关系
     * 
     * @param positions 子元素
     * @return 结果
     */
    int updatePositionChildren(@Param("positions") List<Position> positions);

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
     * @param name 位置名称
     * @param parentId 父位置ID
     * @return 结果
     */
    Position checkPositionNameUnique(@Param("name") String name, @Param("parentId") Long parentId);

    /**
     * 根据ID查询所有子位置
     * 
     * @param id 位置ID
     * @return 位置列表
     */
    List<Position> selectChildrenPositionById(Long id);

}
