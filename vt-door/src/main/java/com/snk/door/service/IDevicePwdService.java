package com.snk.door.service;

import com.snk.door.domain.Device;
import com.snk.door.domain.Pwd;

import java.util.List;

/**
 * 开门密码信息Service接口
 * 
 * @author snk
 * @date 2020-08-03
 */
public interface IDevicePwdService
{
    /**
     * 查询开门密码信息
     *
     * @param id 开门密码信息ID
     * @return 开门密码信息
     */
    Pwd selectPwdById(Long id);

    /**
     * 删除开门密码信息
     *
     * @param deviceIds 设备ID
     * @return 开门密码信息
     */
    int deletePwdByDeviceIds(String deviceIds);

    /**
     * 查询开门密码信息
     *
     * @param sn 设备SN
     * @return 开门密码信息
     */
    List<Pwd> selectPwdBySn(String sn);

    /**
     * 查询开门密码信息列表
     *
     * @param pwd 开门密码信息
     * @return 开门密码信息集合
     */
    List<Pwd> selectPwdList(Pwd pwd);

    /**
     * 新增开门密码信息
     *
     * @param pwdList 开门密码列表
     * @return 结果
     */
    void insertPwd(List<Pwd> pwdList);

    /**
     * 删除开门密码信息
     *
     * @param id 开门密码信息ID
     * @return 结果
     */
    int deletePwdById(Long id);

    /**
     * 批量删除开门密码信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deletePwdByIds(String ids);

    /**
     * 批量删除开门密码信息
     *
     * @param sn 设备SN
     * @return 结果
     */
    int deletePwdBySn(String sn);

}
