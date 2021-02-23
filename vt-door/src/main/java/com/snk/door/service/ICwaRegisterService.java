package com.snk.door.service;

import com.snk.door.domain.CwaRegister;

import java.util.List;

/**
 * 手动登记Service接口
 * 
 * @author snk
 * @date 2020-10-28
 */
public interface ICwaRegisterService
{

    /**
     * 查询手动登记列表
     *
     * @param cwaRegister 手动登记
     * @return 手动登记集合
     */
    List<CwaRegister> selectCwaRegisterList(CwaRegister cwaRegister);

    /**
     * 查询手动登记列表
     *
     * @param cwaRegister 手动登记
     * @return 手动登记集合
     */
    List<CwaRegister> selectCwaRegisterByUserId(CwaRegister cwaRegister);

    /**
     * 新增手动登记
     *
     * @param cwaRegister 手动登记
     * @return 结果
     */
    int insertCwaRegister(CwaRegister cwaRegister);

    /**
     * 删除手动登记
     *
     * @param id 手动登记ID
     * @return 结果
     */
    int deleteCwaRegisterById(Long id);

    /**
     * 批量删除手动登记
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCwaRegisterByIds(String ids);

}
