package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.core.text.Convert;
import com.snk.door.domain.AutoUser;
import com.snk.door.domain.CwaUser;
import com.snk.door.domain.User;
import com.snk.door.mapper.CwaUserMapper;
import com.snk.door.service.ICwaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考勤人员Service业务层处理
 * 
 * @author snk
 * @date 2020-10-28
 */
@Service
public class CwaUserServiceImpl implements ICwaUserService
{
    private static final Logger log = LoggerFactory.getLogger(CwaUserServiceImpl.class);

    @Autowired
    private CwaUserMapper cwaUserMapper;

    /**
     * 查询考勤人员列表
     *
     * @param cwaUser 考勤人员
     * @return 考勤人员集合
     */
    @Override
    @DataScopeSnk(userAlias = "b")
    public List<CwaUser> selectCwaUserList(CwaUser cwaUser) {
        return cwaUserMapper.selectCwaUserList(cwaUser);
    }

    /**
     * 查询所有考勤人员
     *
     * @return 考勤人员集合
     */
    @Override
    public List<CwaUser> selectCwaUserAll() {
        return cwaUserMapper.selectCwaUserList(new CwaUser());
    }

    /**
     * 查询人员信息列表
     *
     * @return 人员信息集合
     */
    @Override
    @DataScopeSnk(userAlias = "b")
    public List<AutoUser> selectUserAutoList() {
        return cwaUserMapper.selectUserAutoList(new User());
    }

    /**
     * 查询考勤人员列表
     *
     * @param userId 人员ID
     * @return 考勤人员集合
     */
    @Override
    public CwaUser selectCwaUserByUserId(Long userId) {
        return cwaUserMapper.selectCwaUserByUserId(userId);
    }

    /**
     * 新增考勤人员
     *
     * @param cwaUser 考勤人员
     * @return 结果
     */
    @Override
    public int insertCwaUser(CwaUser cwaUser) {
        return cwaUserMapper.insertCwaUser(cwaUser);
    }

    /**
     * 删除考勤人员
     *
     * @param id 考勤人员ID
     * @return 结果
     */
    @Override
    public int deleteCwaUserById(Long id) {
        return cwaUserMapper.deleteCwaUserById(id);
    }

    /**
     * 批量删除考勤人员
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCwaUserByIds(String ids) {
        return cwaUserMapper.deleteCwaUserByIds(Convert.toStrArray(ids));
    }

}
