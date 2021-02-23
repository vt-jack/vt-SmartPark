package com.snk.door.mapper;

import com.snk.door.domain.CwaDevice;

import java.util.List;

/**
 * 考勤设备Mapper接口
 * 
 * @author snk
 * @date 2020-10-28
 */
public interface CwaDeviceMapper
{
    /**
     * 查询考勤设备列表
     * 
     * @param cwaDevice 考勤设备
     * @return 考勤设备集合
     */
    List<CwaDevice> selectCwaDeviceList(CwaDevice cwaDevice);

    /**
     * 新增考勤设备
     * 
     * @param cwaDevice 考勤设备
     * @return 结果
     */
    int insertCwaDevice(CwaDevice cwaDevice);

    /**
     * 删除考勤设备
     * 
     * @param id 考勤设备ID
     * @return 结果
     */
    int deleteCwaDeviceById(Long id);

    /**
     * 批量删除考勤设备
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCwaDeviceByIds(String[] ids);

}
