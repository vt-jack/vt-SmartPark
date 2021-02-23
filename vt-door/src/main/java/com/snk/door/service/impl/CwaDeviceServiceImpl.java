package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.core.text.Convert;
import com.snk.door.domain.CwaDevice;
import com.snk.door.mapper.CwaDeviceMapper;
import com.snk.door.service.ICwaDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考勤设备Service业务层处理
 * 
 * @author snk
 * @date 2020-10-28
 */
@Service
public class CwaDeviceServiceImpl implements ICwaDeviceService
{
    private static final Logger log = LoggerFactory.getLogger(CwaDeviceServiceImpl.class);

    @Autowired
    private CwaDeviceMapper cwaDeviceMapper;

    /**
     * 查询考勤设备列表
     *
     * @param cwaDevice 考勤设备
     * @return 考勤设备集合
     */
    @Override
    @DataScopeSnk(deviceAlias = "b")
    public List<CwaDevice> selectCwaDeviceList(CwaDevice cwaDevice) {
        return cwaDeviceMapper.selectCwaDeviceList(cwaDevice);
    }

    /**
     * 查询所有考勤设备
     *
     * @return 考勤设备集合
     */
    @Override
    public List<CwaDevice> selectCwaDeviceAll() {
        return cwaDeviceMapper.selectCwaDeviceList(new CwaDevice());
    }

    /**
     * 新增考勤设备
     *
     * @param cwaDevice 考勤设备
     * @return 结果
     */
    @Override
    public int insertCwaDevice(CwaDevice cwaDevice) {
        return cwaDeviceMapper.insertCwaDevice(cwaDevice);
    }

    /**
     * 删除考勤设备
     *
     * @param id 考勤设备ID
     * @return 结果
     */
    @Override
    public int deleteCwaDeviceById(Long id) {
        return cwaDeviceMapper.deleteCwaDeviceById(id);
    }

    /**
     * 批量删除考勤设备
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCwaDeviceByIds(String ids) {
        return cwaDeviceMapper.deleteCwaDeviceByIds(Convert.toStrArray(ids));
    }

}
