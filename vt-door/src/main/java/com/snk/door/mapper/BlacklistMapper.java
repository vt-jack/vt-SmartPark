package com.snk.door.mapper;

import com.snk.door.domain.Blacklist;

import java.util.List;

/**
 * 黑名单Mapper接口
 * 
 * @author snk
 * @date 2020-08-06
 */
public interface BlacklistMapper 
{
    /**
     * 查询黑名单
     * 
     * @param id 黑名单ID
     * @return 黑名单
     */
    Blacklist selectBlacklistById(Long id);

    /**
     * 查询黑名单
     *
     * @param ids 黑名单ID集合
     * @return 黑名单
     */
    List<Blacklist> selectBlacklistByIds(String[] ids);

    /**
     * 查询黑名单列表
     * 
     * @param blacklist 黑名单
     * @return 黑名单集合
     */
    List<Blacklist> selectBlacklistList(Blacklist blacklist);

    /**
     * 根据解禁时间获取解禁列表
     * @return
     */
    List<Blacklist> selectLiftList();

    /**
     * 新增黑名单
     * 
     * @param blacklist 黑名单
     * @return 结果
     */
    int insertBlacklist(Blacklist blacklist);

    /**
     * 批量新增黑名单
     * @param list
     * @return
     */
    int insertBlacklistBatch(List<Blacklist> list);

    /**
     * 修改黑名单
     * 
     * @param blacklist 黑名单
     * @return 结果
     */
    int updateBlacklist(Blacklist blacklist);

    /**
     * 删除黑名单
     * 
     * @param id 黑名单ID
     * @return 结果
     */
    int deleteBlacklistById(Long id);

    /**
     * 删除黑名单信息
     *
     * @param userIds 人员ID
     * @return 结果
     */
    int deleteBlacklistByUserIds(String[] userIds);

    /**
     * 批量删除黑名单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteBlacklistByIds(String[] ids);
}
