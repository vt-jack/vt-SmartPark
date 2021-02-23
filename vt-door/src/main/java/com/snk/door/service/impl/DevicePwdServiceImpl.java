package com.snk.door.service.impl;

import com.snk.common.core.text.Convert;
import com.snk.door.domain.Pwd;
import com.snk.door.mapper.DevicePwdMapper;
import com.snk.door.service.IDevicePwdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 开门密码信息Service业务层处理
 * 
 * @author snk
 * @date 2020-08-03
 */
@Service
public class DevicePwdServiceImpl implements IDevicePwdService
{
    private static final Logger log = LoggerFactory.getLogger(DevicePwdServiceImpl.class);

    @Autowired
    private DevicePwdMapper devicePwdMapper;

    /**
     * 查询开门密码信息
     * 
     * @param id 开门密码信息ID
     * @return 开门密码信息
     */
    @Override
    public Pwd selectPwdById(Long id)
    {
        return devicePwdMapper.selectPwdById(id);
    }

    /**
     * 删除开门密码信息
     *
     * @param deviceIds 设备ID
     * @return 开门密码信息
     */
    @Override
    public int deletePwdByDeviceIds(String deviceIds)
    {
        return devicePwdMapper.deletePwdByDeviceIds(Convert.toStrArray(deviceIds));
    }

    /**
     * 查询开门密码信息
     *
     * @param sn 设备SN
     * @return 开门密码信息
     */
    @Override
    public List<Pwd> selectPwdBySn(String sn)
    {
        return devicePwdMapper.selectPwdBySn(sn);
    }

    /**
     * 查询开门密码信息列表
     * 
     * @param Pwd 开门密码信息
     * @return 开门密码信息
     */
    @Override
    public List<Pwd> selectPwdList(Pwd Pwd)
    {
        return devicePwdMapper.selectPwdList(Pwd);
    }

    /**
     * 新增开门密码信息
     * 
     * @param pwdList 开门密码列表
     * @return 结果
     */
    @Override
    public void insertPwd(List<Pwd> pwdList) {
        pwdList.stream().forEach(pwd -> {
            if (devicePwdMapper.updatePwd(pwd) == 0) {
                devicePwdMapper.insertPwd(pwd);
            }
        });
    }

    /**
     * 删除开门密码信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePwdByIds(String ids)
    {
        return devicePwdMapper.deletePwdByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除开门密码信息信息
     *
     * @param id 开门密码信息ID
     * @return 结果
     */
    @Override
    public int deletePwdById(Long id)
    {
        return devicePwdMapper.deletePwdById(id);
    }

    /**
     * 批量删除开门密码信息
     *
     * @param sn 设备SN
     * @return 结果
     */
    @Override
    public int deletePwdBySn(String sn) {
        return devicePwdMapper.deletePwdBySn(sn);
    }

}
