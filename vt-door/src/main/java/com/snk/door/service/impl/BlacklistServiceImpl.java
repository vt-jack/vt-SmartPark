package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.core.text.Convert;
import com.snk.common.exception.BusinessException;
import com.snk.common.utils.DateUtils;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Blacklist;
import com.snk.door.enums.CardStatus;
import com.snk.door.mapper.BlacklistMapper;
import com.snk.door.service.IBlacklistService;
import com.snk.door.service.IDeviceAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单Service业务层处理
 * 
 * @author snk
 * @date 2020-08-06
 */
@Service
public class BlacklistServiceImpl implements IBlacklistService 
{
    @Autowired
    private BlacklistMapper blacklistMapper;
    @Autowired
    private IDeviceAuthService deviceAuthService;

    /**
     * 查询黑名单
     * 
     * @param id 黑名单ID
     * @return 黑名单
     */
    @Override
    public Blacklist selectBlacklistById(Long id)
    {
        return blacklistMapper.selectBlacklistById(id);
    }

    /**
     * 查询黑名单列表
     * 
     * @param blacklist 黑名单
     * @return 黑名单
     */
    @Override
    @DataScopeSnk(userAlias = "b")
    public List<Blacklist> selectBlacklistList(Blacklist blacklist)
    {
        return blacklistMapper.selectBlacklistList(blacklist);
    }

    /**
     * 根据解禁时间获取解禁列表
     * @return
     */
    @Override
    public List<Blacklist> selectLiftList() {
        return blacklistMapper.selectLiftList();
    }

    /**
     * 新增黑名单
     * 
     * @param blacklist 黑名单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertBlacklist(Blacklist blacklist)
    {
        blacklist.setCreateTime(DateUtils.getNowDate());
        return blacklistMapper.insertBlacklist(blacklist);
    }

    /**
     * 批量新增黑名单
     *
     * @param list 黑名单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertBlacklistBatch(List<Blacklist> list)
    {
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException("选中的人员已在黑名单当中！");
        }
        this.uploadAuth(list, CardStatus.BLACKLIST);
        return blacklistMapper.insertBlacklistBatch(list);
    }

    /**
     * 修改黑名单
     * 
     * @param blacklist 黑名单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateBlacklist(Blacklist blacklist)
    {
        blacklist.setUpdateTime(DateUtils.getNowDate());
        return blacklistMapper.updateBlacklist(blacklist);
    }

    /**
     * 删除黑名单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBlacklistByIds(String ids)
    {
        String[] array = Convert.toStrArray(ids);
        this.uploadAuth(blacklistMapper.selectBlacklistByIds(array), CardStatus.NORMAL);
        return blacklistMapper.deleteBlacklistByIds(array);
    }

    /**
     * 上传权限到控制器
     *
     * @param list 黑名单集合
     * @param cardStatus 卡片状态
     */
    private void uploadAuth(List<Blacklist> list, CardStatus cardStatus) {
        List<Auth> authList = new ArrayList<>();
        list.stream().forEach(item -> {
            List<Auth> userAuth = deviceAuthService.selectAuthByUserId(item.getRefId());
            userAuth.stream().forEach(item1 -> item1.setCardStatus(cardStatus.getValue()));
            authList.addAll(userAuth);
        });
        if (!CollectionUtils.isEmpty(authList)) {
            deviceAuthService.uploadAuth(authList); // 上传权限到控制器
        }
    }

    /**
     * 删除黑名单信息
     * 
     * @param id 黑名单ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBlacklistById(Long id)
    {
        return blacklistMapper.deleteBlacklistById(id);
    }

    /**
     * 删除黑名单信息
     *
     * @param userIds 人员ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBlacklistByUserIds(String userIds)
    {
        return blacklistMapper.deleteBlacklistByUserIds(Convert.toStrArray(userIds));
    }
}
