package com.snk.door.mapper;

import com.snk.door.domain.VisitReg;

import java.util.List;

/**
 * 来访登记Mapper接口
 * 
 * @author snk
 * @date 2020-11-18
 */
public interface VisitRegMapper
{
    /**
     * 查询来访登记
     * 
     * @param id 来访登记ID
     * @return 来访登记
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
     * 查询来访登记列表
     * 
     * @param visitReg 来访登记
     * @return 来访登记集合
     */
    List<VisitReg> selectVisitRegList(VisitReg visitReg);

    /**
     * 新增来访登记
     * 
     * @param visitReg 来访登记
     * @return 结果
     */
    int insertVisitReg(VisitReg visitReg);

    /**
     * 修改来访登记
     * 
     * @param visitReg 来访登记
     * @return 结果
     */
    int updateVisitReg(VisitReg visitReg);

    /**
     * 删除来访登记
     * 
     * @param id 来访登记ID
     * @return 结果
     */
    int deleteVisitRegById(Long id);

    /**
     * 批量删除来访登记
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteVisitRegByIds(String[] ids);

}
