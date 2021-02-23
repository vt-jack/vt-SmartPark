package com.snk.door.mapper;

import com.snk.door.domain.TimeSlot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 开门时段Mapper接口
 * 
 * @author snk
 * @date 2020-08-03
 */
public interface TimeSlotMapper
{
    /**
     * 查询开门时段
     * 
     * @param id 开门时段ID
     * @return 开门时段
     */
    TimeSlot selectTimeSlotById(Long id);

    /**
     * 查询开门时段
     *
     * @param sn 设备SN
     * @param idx 时段下标
     * @return 开门时段
     */
    TimeSlot selectTimeSlotByIdx(@Param("sn") String sn, @Param("idx") Integer idx);

    /**
     * 查询最大时段1-64
     *
     * @param sn 设备SN
     * @return idx
     */
    int selectMaxIdx(String sn);

    /**
     * 查询开门时段列表
     *
     * @param timeSlot 开门时段
     * @return 开门时段集合
     */
    List<TimeSlot> selectTimeSlotList(TimeSlot timeSlot);

    /**
     * 新增开门时段
     * 
     * @param timeSlot 开门时段
     * @return 结果
     */
    int insertTimeSlot(TimeSlot timeSlot);

    /**
     * 修改开门时段
     * 
     * @param timeSlot 开门时段
     * @return 结果
     */
    int updateTimeSlot(TimeSlot timeSlot);

    /**
     * 删除开门时段
     * 
     * @param id 开门时段ID
     * @return 结果
     */
    int deleteTimeSlotById(Long id);

    /**
     * 删除开门时段信息
     *
     * @param sn 设备sn
     * @param idx 时段下标
     * @return 结果
     */
    int deleteTimeSlotByIdx(@Param("sn") String sn, @Param("idx") Integer idx);

    /**
     * 批量删除开门时段
     * 
     * @param sn 设备SN
     * @return 结果
     */
    int deleteTimeSlotBySn(String sn);

}
