package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.core.text.Convert;
import com.snk.door.domain.CwaRegister;
import com.snk.door.mapper.CwaRegisterMapper;
import com.snk.door.service.ICwaRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手动登记Service业务层处理
 * 
 * @author snk
 * @date 2020-10-28
 */
@Service
public class CwaRegisterServiceImpl implements ICwaRegisterService
{
    private static final Logger log = LoggerFactory.getLogger(CwaRegisterServiceImpl.class);

    @Autowired
    private CwaRegisterMapper cwaRegisterMapper;

    /**
     * 查询手动登记列表
     *
     * @param cwaRegister 手动登记
     * @return 手动登记集合
     */
    @Override
    @DataScopeSnk(userAlias = "b")
    public List<CwaRegister> selectCwaRegisterList(CwaRegister cwaRegister) {
        return cwaRegisterMapper.selectCwaRegisterList(cwaRegister);
    }

    /**
     * 查询手动登记列表
     *
     * @param cwaRegister 手动登记
     * @return 手动登记集合
     */
    @Override
    public List<CwaRegister> selectCwaRegisterByUserId(CwaRegister cwaRegister) {
        return cwaRegisterMapper.selectCwaRegisterByUserId(cwaRegister);
    }

    /**
     * 新增手动登记
     *
     * @param cwaRegister 手动登记
     * @return 结果
     */
    @Override
    public int insertCwaRegister(CwaRegister cwaRegister) {
        return cwaRegisterMapper.insertCwaRegister(cwaRegister);
    }

    /**
     * 删除手动登记
     *
     * @param id 手动登记ID
     * @return 结果
     */
    @Override
    public int deleteCwaRegisterById(Long id) {
        return cwaRegisterMapper.deleteCwaRegisterById(id);
    }

    /**
     * 批量删除手动登记
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCwaRegisterByIds(String ids) {
        return cwaRegisterMapper.deleteCwaRegisterByIds(Convert.toStrArray(ids));
    }

}
