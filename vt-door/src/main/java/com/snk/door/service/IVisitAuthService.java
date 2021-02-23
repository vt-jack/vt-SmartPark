package com.snk.door.service;

import com.snk.door.domain.VisitAuth;

import java.util.List;

/**
 * 访问权限Service接口
 * 
 * @author snk
 * @date 2020-11-18
 */
public interface IVisitAuthService
{
    /**
     * 查询访问权限
     *
     * @param id 访问权限ID
     * @return 访问权限
     */
    VisitAuth selectVisitAuthById(Long id);

    /**
     * 查询访问权限列表
     *
     * @param visitAuth 访问权限
     * @return 访问权限集合
     */
    List<VisitAuth> selectVisitAuthList(VisitAuth visitAuth);

    /**
     * 新增访问权限
     *
     * @param visitAuth 访问权限
     * @return 结果
     */
    int insertVisitAuth(VisitAuth visitAuth);

    /**
     * 修改访问权限
     *
     * @param visitAuth 访问权限
     * @return 结果
     */
    int updateVisitAuth(VisitAuth visitAuth);

    /**
     * 删除访问权限
     *
     * @param id 访问权限ID
     * @return 结果
     */
    int deleteVisitAuthById(Long id);

    /**
     * 批量删除访问权限
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteVisitAuthByIds(String ids);
}
