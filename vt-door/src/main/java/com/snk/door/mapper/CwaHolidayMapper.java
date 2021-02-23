package com.snk.door.mapper;

import com.snk.door.domain.CwaHoliday;

import java.util.List;

/**
 * 节假日Mapper接口
 * 
 * @author snk
 * @date 2020-10-28
 */
public interface CwaHolidayMapper
{
    /**
     * 查询节假日列表
     * 
     * @param cwaHoliday 节假日
     * @return 节假日集合
     */
    List<CwaHoliday> selectCwaHolidayList(CwaHoliday cwaHoliday);

    /**
     * 查询节假日
     *
     * @param id 主键ID
     * @return 节假日
     */
    CwaHoliday selectCwaHolidayById(Long id);

    /**
     * 新增节假日
     * 
     * @param cwaHoliday 节假日
     * @return 结果
     */
    int insertCwaHoliday(CwaHoliday cwaHoliday);

    /**
     * 修改节假日
     *
     * @param cwaHoliday 节假日
     * @return 结果
     */
    int updateCwaHoliday(CwaHoliday cwaHoliday);

    /**
     * 删除节假日
     * 
     * @param id 节假日ID
     * @return 结果
     */
    int deleteCwaHolidayById(Long id);

    /**
     * 批量删除节假日
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCwaHolidayByIds(String[] ids);

}
