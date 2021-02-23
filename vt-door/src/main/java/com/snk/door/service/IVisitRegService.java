package com.snk.door.service;

import com.snk.door.domain.User;
import com.snk.door.domain.VisitReg;

import java.util.List;

/**
 * 企业信息Service接口
 * 
 * @author snk
 * @date 2020-11-18
 */
public interface IVisitRegService
{
    /**
     * 查询企业信息
     *
     * @param id 企业信息ID
     * @return 企业信息
     */
    VisitReg selectVisitRegById(Long id);

    /**
     * 查询来访登记
     *
     * @param cardNo 卡号
     * @return 来访登记
     */
    VisitReg selectVisitRegByCardNo(String cardNo);

    /**
     * 根据卡号或者手机号查询登记信息
     * @param visitReg
     * @return
     */
    List<VisitReg> selectBySearchValue(VisitReg visitReg);

    /**
     * 查询企业信息列表
     *
     * @param visitReg 企业信息
     * @return 企业信息集合
     */
    List<VisitReg> selectVisitRegList(VisitReg visitReg);

    /**
     * 新增企业信息
     *
     * @param visitReg 企业信息
     * @return 结果
     */
    int insertVisitReg(VisitReg visitReg);

    /**
     * 修改企业信息
     *
     * @param visitReg 企业信息
     * @return 结果
     */
    int updateVisitReg(VisitReg visitReg);

    /**
     * 注销
     *
     * @param id 主键ID
     * @return 结果
     */
    void cancel(Long id);

    /**
     * 删除企业信息
     *
     * @param id 企业信息ID
     * @return 结果
     */
    int deleteVisitRegById(Long id);

    /**
     * 批量删除企业信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteVisitRegByIds(String ids);

    /**
     * 校验唯一
     * @param visitReg
     * @return
     */
    String checkUnique(VisitReg visitReg);
}
