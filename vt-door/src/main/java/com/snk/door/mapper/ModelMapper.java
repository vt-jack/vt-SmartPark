package com.snk.door.mapper;

import com.snk.door.domain.Model;
import com.snk.door.domain.User;

import java.util.List;

/**
 * 设备型号信息Mapper接口
 * 
 * @author snk
 * @date 2020-08-03
 */
public interface ModelMapper
{
    /**
     * 查询设备型号信息
     * 
     * @param id 设备型号信息ID
     * @return 设备型号信息
     */
    Model selectModelById(Long id);

    /**
     * 查询设备型号信息
     *
     * @param model 设备型号
     * @return 设备型号信息
     */
    Model selectModelByModel(String model);

    /**
     * 查询设备型号信息列表
     * 
     * @param model 设备型号信息
     * @return 设备型号信息集合
     */
    List<Model> selectModelList(Model model);

    /**
     * 新增设备型号信息
     * 
     * @param model 设备型号信息
     * @return 结果
     */
    int insertModel(Model model);

    /**
     * 修改设备型号信息
     * 
     * @param model 设备型号信息
     * @return 结果
     */
    int updateModel(Model model);

    /**
     * 删除设备型号信息
     * 
     * @param id 设备型号信息ID
     * @return 结果
     */
    int deleteModelById(Long id);

    /**
     * 批量删除设备型号信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteModelByIds(String[] ids);

}
