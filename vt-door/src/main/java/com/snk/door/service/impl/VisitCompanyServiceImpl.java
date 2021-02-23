package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.core.text.Convert;
import com.snk.common.utils.DateUtils;
import com.snk.door.domain.Proof;
import com.snk.door.domain.VisitCompany;
import com.snk.door.mapper.ProofMapper;
import com.snk.door.mapper.VisitCompanyMapper;
import com.snk.door.service.IProofService;
import com.snk.door.service.IVisitCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 企业Service业务层处理
 * 
 * @author snk
 * @date 2020-11-18
 */
@Service
public class VisitCompanyServiceImpl implements IVisitCompanyService
{
    @Autowired
    private VisitCompanyMapper visitCompanyMapper;

    /**
     * 查询企业信息
     *
     * @param id 企业信息ID
     * @return 企业信息
     */
    @Override
    public VisitCompany selectVisitCompanyById(Long id) {
        return visitCompanyMapper.selectVisitCompanyById(id);
    }

    /**
     * 查询企业信息列表
     *
     * @param visitCompany 企业信息
     * @return 企业信息集合
     */
    @Override
    @DataScopeSnk(userAlias = "b")
    public List<VisitCompany> selectVisitCompanyList(VisitCompany visitCompany) {
        return visitCompanyMapper.selectVisitCompanyList(visitCompany);
    }

    /**
     * 新增企业信息
     *
     * @param visitCompany 企业信息
     * @return 结果
     */
    @Override
    public int insertVisitCompany(VisitCompany visitCompany) {
        return visitCompanyMapper.insertVisitCompany(visitCompany);
    }

    /**
     * 修改企业信息
     *
     * @param visitCompany 企业信息
     * @return 结果
     */
    @Override
    public int updateVisitCompany(VisitCompany visitCompany) {
        return visitCompanyMapper.updateVisitCompany(visitCompany);
    }

    /**
     * 删除企业信息
     *
     * @param id 企业信息ID
     * @return 结果
     */
    @Override
    public int deleteVisitCompanyById(Long id) {
        return visitCompanyMapper.deleteVisitCompanyById(id);
    }

    /**
     * 批量删除企业信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteVisitCompanyByIds(String ids) {
        return visitCompanyMapper.deleteVisitCompanyByIds(Convert.toStrArray(ids));
    }

}
