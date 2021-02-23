package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.core.text.Convert;
import com.snk.door.domain.VisitAuth;
import com.snk.door.mapper.VisitAuthMapper;
import com.snk.door.service.IVisitAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业Service业务层处理
 * 
 * @author snk
 * @date 2020-11-18
 */
@Service
public class VisitAuthServiceImpl implements IVisitAuthService
{
    @Autowired
    private VisitAuthMapper visitAuthMapper;

    /**
     * 查询访问权限
     *
     * @param id 访问权限ID
     * @return 访问权限
     */
    @Override
    public VisitAuth selectVisitAuthById(Long id) {
        return visitAuthMapper.selectVisitAuthById(id);
    }

    /**
     * 查询访问权限列表
     *
     * @param visitAuth 访问权限
     * @return 访问权限集合
     */
    @Override
    @DataScopeSnk(userAlias = "c")
    public List<VisitAuth> selectVisitAuthList(VisitAuth visitAuth) {
        return visitAuthMapper.selectVisitAuthList(visitAuth);
    }

    /**
     * 新增访问权限
     *
     * @param visitAuth 访问权限
     * @return 结果
     */
    @Override
    public int insertVisitAuth(VisitAuth visitAuth) {
        return visitAuthMapper.insertVisitAuth(visitAuth);
    }

    /**
     * 修改访问权限
     *
     * @param visitAuth 访问权限
     * @return 结果
     */
    @Override
    public int updateVisitAuth(VisitAuth visitAuth) {
        return visitAuthMapper.updateVisitAuth(visitAuth);
    }

    /**
     * 删除访问权限
     *
     * @param id 访问权限ID
     * @return 结果
     */
    @Override
    public int deleteVisitAuthById(Long id) {
        return visitAuthMapper.deleteVisitAuthById(id);
    }

    /**
     * 批量删除访问权限
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteVisitAuthByIds(String ids) {
        return visitAuthMapper.deleteVisitAuthByIds(Convert.toStrArray(ids));
    }

}
