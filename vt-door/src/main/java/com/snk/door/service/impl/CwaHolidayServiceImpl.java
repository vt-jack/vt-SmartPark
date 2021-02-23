package com.snk.door.service.impl;

import com.snk.common.core.text.Convert;
import com.snk.door.domain.CwaHoliday;
import com.snk.door.mapper.CwaHolidayMapper;
import com.snk.door.service.ICwaHolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 节假日Service业务层处理
 * 
 * @author snk
 * @date 2020-10-28
 */
@Service
public class CwaHolidayServiceImpl implements ICwaHolidayService
{
    private static final Logger log = LoggerFactory.getLogger(CwaHolidayServiceImpl.class);

    @Autowired
    private CwaHolidayMapper cwaHolidayMapper;

    /**
     * 查询节假日列表
     *
     * @param cwaHoliday 节假日
     * @return 节假日集合
     */
    @Override
    public List<CwaHoliday> selectCwaHolidayList(CwaHoliday cwaHoliday) {
        return cwaHolidayMapper.selectCwaHolidayList(cwaHoliday);
    }

    /**
     * 查询节假日
     *
     * @param id 主键ID
     * @return 节假日
     */
    @Override
    public CwaHoliday selectCwaHolidayById(Long id) {
        return cwaHolidayMapper.selectCwaHolidayById(id);
    }

    /**
     * 新增节假日
     *
     * @param cwaHoliday 节假日
     * @return 结果
     */
    @Override
    public int insertCwaHoliday(CwaHoliday cwaHoliday) {
        return cwaHolidayMapper.insertCwaHoliday(cwaHoliday);
    }

    /**
     * 修改节假日
     *
     * @param cwaHoliday 节假日
     * @return 结果
     */
    @Override
    public int updateCwaHoliday(CwaHoliday cwaHoliday) {
        return cwaHolidayMapper.updateCwaHoliday(cwaHoliday);
    }

    /**
     * 删除节假日
     *
     * @param id 节假日ID
     * @return 结果
     */
    @Override
    public int deleteCwaHolidayById(Long id) {
        return cwaHolidayMapper.deleteCwaHolidayById(id);
    }

    /**
     * 批量删除节假日
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCwaHolidayByIds(String ids) {
        return cwaHolidayMapper.deleteCwaHolidayByIds(Convert.toStrArray(ids));
    }

}
