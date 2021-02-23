package com.snk.door.service;

import com.snk.door.domain.VisitCompany;

import java.util.List;

/**
 * 企业信息Service接口
 * 
 * @author snk
 * @date 2020-11-18
 */
public interface IVisitCompanyService
{
    /**
     * 查询企业信息
     *
     * @param id 企业信息ID
     * @return 企业信息
     */
    VisitCompany selectVisitCompanyById(Long id);

    /**
     * 查询企业信息列表
     *
     * @param visitCompany 企业信息
     * @return 企业信息集合
     */
    List<VisitCompany> selectVisitCompanyList(VisitCompany visitCompany);

    /**
     * 新增企业信息
     *
     * @param visitCompany 企业信息
     * @return 结果
     */
    int insertVisitCompany(VisitCompany visitCompany);

    /**
     * 修改企业信息
     *
     * @param visitCompany 企业信息
     * @return 结果
     */
    int updateVisitCompany(VisitCompany visitCompany);

    /**
     * 删除企业信息
     *
     * @param id 企业信息ID
     * @return 结果
     */
    int deleteVisitCompanyById(Long id);

    /**
     * 批量删除企业信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteVisitCompanyByIds(String ids);
}
