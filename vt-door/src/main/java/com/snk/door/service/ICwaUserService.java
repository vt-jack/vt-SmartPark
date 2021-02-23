package com.snk.door.service;

import com.snk.door.domain.AutoUser;
import com.snk.door.domain.CwaUser;

import java.util.List;

/**
 * 考勤人员Service接口
 * 
 * @author snk
 * @date 2020-10-28
 */
public interface ICwaUserService
{

    /**
     * 查询考勤人员列表
     *
     * @param cwaUser 考勤人员
     * @return 考勤人员集合
     */
    List<CwaUser> selectCwaUserList(CwaUser cwaUser);

    /**
     * 查询所有考勤人员
     *
     * @return 考勤人员集合
     */
    List<CwaUser> selectCwaUserAll();

    /**
     * 查询人员信息列表
     *
     * @return 人员信息集合
     */
    List<AutoUser> selectUserAutoList();

    /**
     * 查询考勤人员列表
     *
     * @param userId 人员ID
     * @return 考勤人员集合
     */
    CwaUser selectCwaUserByUserId(Long userId);

    /**
     * 新增考勤人员
     *
     * @param cwaUser 考勤人员
     * @return 结果
     */
    int insertCwaUser(CwaUser cwaUser);

    /**
     * 删除考勤人员
     *
     * @param id 考勤人员ID
     * @return 结果
     */
    int deleteCwaUserById(Long id);

    /**
     * 批量删除考勤人员
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCwaUserByIds(String ids);

}
