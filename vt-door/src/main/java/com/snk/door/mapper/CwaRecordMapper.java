package com.snk.door.mapper;

import com.snk.door.domain.CwaRecord;

import java.util.List;

/**
 * 考勤报表Mapper接口
 * 
 * @author snk
 * @date 2020-10-28
 */
public interface CwaRecordMapper
{
    /**
     * 查询考勤报表列表
     * 
     * @param cwaRecord 考勤报表
     * @return 考勤报表集合
     */
    List<CwaRecord> selectCwaRecordList(CwaRecord cwaRecord);

    /**
     * 查询考勤报表
     *
     * @param id 主键ID
     * @return 考勤报表
     */
    CwaRecord selectCwaRecordById(Long id);

    /**
     * 新增考勤报表
     * 
     * @param cwaRecord 考勤报表
     * @return 结果
     */
    int insertCwaRecord(CwaRecord cwaRecord);

    /**
     * 修改考勤报表
     *
     * @param cwaRecord 考勤报表
     * @return 结果
     */
    int updateCwaRecord(CwaRecord cwaRecord);

    /**
     * 删除考勤报表
     * 
     * @param id 考勤报表ID
     * @return 结果
     */
    int deleteCwaRecordById(Long id);

    /**
     * 批量删除考勤报表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCwaRecordByIds(String[] ids);

}
