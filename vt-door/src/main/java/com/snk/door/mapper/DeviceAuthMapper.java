package com.snk.door.mapper;

import com.snk.door.domain.Auth;

import java.util.List;

/**
 * 开门权限Mapper接口
 * 
 * @author snk
 * @date 2020-08-06
 */
public interface DeviceAuthMapper
{
    /**
     * 查询开门权限
     * 
     * @param id 开门权限ID
     * @return 开门权限
     */
    Auth selectAuthById(Long id);

    /**
     * 查询开门权限
     *
     * @param userId 人员ID
     * @return 开门权限
     */
    List<Auth> selectAuthByUserId(Long userId);

    /**
     * 删除开门权限
     *
     * @param deviceIds 设备ID
     * @return 开门权限
     */
    int deleteAuthByDeviceIds(String[] deviceIds);

    /**
     * 新增开门权限
     *
     * @param auth 开门权限
     * @return 结果
     */
    int insertAuth(Auth auth);

    /**
     * 查询开门权限
     *
     * @param auth 开门权限
     * @return 开门权限
     */
    Auth selectAuth(Auth auth);

    /**
     * 查询开门权限列表
     *
     * @param auth 开门权限
     * @return 开门权限集合
     */
    List<Auth> selectAuthList(Auth auth);

    /**
     * 修改开门权限
     *
     * @param auth 开门权限
     * @return 结果
     */
    int updateAuth(Auth auth);

    /**
     * 删除开门权限
     * 
     * @param auth 开门权限
     * @return 结果
     */
    int deleteAuth(Auth auth);

    /**
     * 删除开门权限
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteAuthByUserId(Long userId);

    /**
     * 删除开门权限
     *
     * @param sn 设备SN
     * @return 结果
     */
    int deleteAuthBySn(String sn);

    /**
     * 批量删除开门权限
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDeviceAuthByIds(String[] ids);
}
