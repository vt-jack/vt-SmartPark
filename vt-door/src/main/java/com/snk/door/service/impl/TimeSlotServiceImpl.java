package com.snk.door.service.impl;

import com.alibaba.fastjson.JSON;
import com.snk.common.constant.Constants;
import com.snk.common.constant.UserConstants;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.control.write.WriteTimeGroupService;
import com.snk.door.domain.Device;
import com.snk.door.domain.TimeSlot;
import com.snk.door.mapper.TimeSlotMapper;
import com.snk.door.service.ITimeSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开门时段Service业务层处理
 * 
 * @author snk
 * @date 2020-08-03
 */
@Service
public class TimeSlotServiceImpl implements ITimeSlotService
{
    private static final Logger log = LoggerFactory.getLogger(TimeSlotServiceImpl.class);

    @Autowired
    private TimeSlotMapper timeSlotMapper;
    @Autowired
    private WriteTimeGroupService writeTimeGroupService;

    /**
     * 查询开门时段
     *
     * @param id 开门时段ID
     * @return 开门时段
     */
    @Override
    public TimeSlot selectTimeSlotById(Long id) {
        return timeSlotMapper.selectTimeSlotById(id);
    }

    /**
     * 查询开门时段
     *
     * @param sn 设备SN
     * @param idx 时段下标
     * @return 开门时段
     */
    @Override
    public TimeSlot selectTimeSlotByIdx(String sn, Integer idx) {
        return timeSlotMapper.selectTimeSlotByIdx(sn, idx);
    }

    /**
     * 查询最大时段1-64
     *
     * @param sn 设备SN
     * @return idx
     */
    @Override
    public int selectMaxIdx(String sn) {
        return timeSlotMapper.selectMaxIdx(sn);
    }

    /**
     * 查询所有开门时段列表
     *
     * @param userId 用户ID
     * @return 开门时段集合
     */
    @Override
    public List<TimeSlot> selectTimeSlotListByUserId(Long userId) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSn(Constants.TIME_SLOT_DEFAULT_SN);
        List<TimeSlot> defaultTs = this.selectTimeSlotList(timeSlot);
        timeSlot.setSn(Constants.TIME_SLOT_DEFAULT_SN + userId);
        defaultTs.addAll(this.selectTimeSlotList(timeSlot));
        return defaultTs;
    }

    /**
     * 查询开门时段列表
     *
     * @param timeSlot 开门时段
     * @return 开门时段集合
     */
    @Override
    public List<TimeSlot> selectTimeSlotList(TimeSlot timeSlot) {
        return timeSlotMapper.selectTimeSlotList(timeSlot);
    }

    /**
     * 新增开门时段
     *
     * @param timeSlot 开门时段
     * @return 结果
     */
    @Override
    public int insertTimeSlot(TimeSlot timeSlot) {
        return timeSlotMapper.insertTimeSlot(timeSlot);
    }

    /**
     * 新增默认开门时段
     *
     * @param device 设备信息
     * @return 结果
     */
    @Override
    public void insertDefaultTimeSlot(Device device) {
        TimeSlot timeSlot = timeSlotMapper.selectTimeSlotByIdx(device.getSn(), 1);
        if (ObjectUtils.isEmpty(timeSlot)) {
            timeSlot = new TimeSlot();
            timeSlot.setSn(device.getSn());
            timeSlot.setName("开门时段01");
            timeSlot.setIdx(1);
            timeSlot.setTimeGroup(getDefaultTimeGroup());
        }
        device.setParam(timeSlot);
        writeTimeGroupService.writeTimeGroup(device);
    }

    /**
     * 获取默认时间段字符串
     * @return
     */
    private String getDefaultTimeGroup() {
        Map<String, List<Map<String, String>>> map = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            List<Map<String, String>> list = new ArrayList<>();
            Map<String, String> map1 = new HashMap<>();
            map1.put("start", "00:00");
            map1.put("end", "23:59");
            list.add(map1);
            map.put("time"+i, list);
        }
        return JSON.toJSONString(map);
    }

    /**
     * 修改开门时段
     *
     * @param timeSlot 开门时段
     * @return 结果
     */
    @Override
    public int updateTimeSlot(TimeSlot timeSlot) {
        return timeSlotMapper.updateTimeSlot(timeSlot);
    }

    /**
     * 删除开门时段信息
     *
     * @param id 开门时段ID
     * @return 结果
     */
    @Override
    public int deleteTimeSlotById(Long id) {
        return timeSlotMapper.deleteTimeSlotById(id);
    }

    /**
     * 删除开门时段信息
     *
     * @param sn 设备sn
     * @param idx 时段下标
     * @return 结果
     */
    @Override
    public int deleteTimeSlotByIdx(String sn, Integer idx) {
        return timeSlotMapper.deleteTimeSlotByIdx(sn, idx);
    }

    /**
     * 删除开门时段信息
     *
     * @param sn 设备SN
     * @return 结果
     */
    @Override
    public int deleteTimeSlotBySn(String sn) {
        return timeSlotMapper.deleteTimeSlotBySn(sn);
    }

    /**
     * 校验唯一
     *
     * @param timeSlot 开门时段
     * @return
     */
    @Override
    public String checkUnique(TimeSlot timeSlot)
    {
        Integer idx = StringUtils.isNull(timeSlot.getIdx()) ? -1 : timeSlot.getIdx();
        timeSlot.setDelFlag("0");
        List<TimeSlot> timeSlotList = timeSlotMapper.selectTimeSlotList(timeSlot);
        if (!CollectionUtils.isEmpty(timeSlotList) && timeSlotList.get(0).getIdx() != idx)
        {
            return UserConstants.EXCEPTION;
        }
        return UserConstants.NORMAL;
    }

}
