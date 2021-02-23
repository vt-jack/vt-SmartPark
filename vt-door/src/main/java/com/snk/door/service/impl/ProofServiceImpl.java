package com.snk.door.service.impl;

import com.snk.common.core.text.Convert;
import com.snk.common.utils.DateUtils;
import com.snk.door.domain.Proof;
import com.snk.door.mapper.ProofMapper;
import com.snk.door.service.IProofService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 凭证Service业务层处理
 * 
 * @author snk
 * @date 2020-08-06
 */
@Service
public class ProofServiceImpl implements IProofService 
{
    @Autowired
    private ProofMapper proofMapper;

    /**
     * 查询凭证
     * 
     * @param id 凭证ID
     * @return 凭证
     */
    @Override
    public Proof selectProofById(Long id)
    {
        return proofMapper.selectProofById(id);
    }

    /**
     * 查询凭证列表
     * 
     * @param proof 凭证
     * @return 凭证
     */
    @Override
    public List<Proof> selectProofList(Proof proof)
    {
        return proofMapper.selectProofList(proof);
    }

    /**
     * 查询凭证列表
     *
     * @param userId 人员ID
     * @return 凭证集合
     */
    @Override
    public List<Proof> selectProofByUserId(Long userId) {
        return proofMapper.selectProofByUserId(userId);
    }

    /**
     * 新增凭证
     * 
     * @param proof 凭证
     * @return 结果
     */
    @Transactional
    @Override
    public int insertProof(Proof proof)
    {
        proof.setCreateTime(DateUtils.getNowDate());
        return proofMapper.insertProof(proof);
    }

    /**
     * 批量新增凭证
     *
     * @param list 凭证
     * @return 结果
     */
    @Transactional
    @Override
    public int insertProofBatch(List<Proof> list)
    {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return proofMapper.insertProofBatch(list);
    }

    /**
     * 修改凭证
     * 
     * @param proof 凭证
     * @return 结果
     */
    @Transactional
    @Override
    public int updateProof(Proof proof)
    {
        proof.setUpdateTime(DateUtils.getNowDate());
        return proofMapper.updateProof(proof);
    }

    /**
     * 批量修改凭证
     *
     * @param list 凭证
     * @return 结果
     */
    @Transactional
    @Override
    public int updateProofBatch(List<Proof> list)
    {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return proofMapper.updateProofBatch(list);
    }

    /**
     * 删除凭证对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProofByIds(String ids)
    {
        return proofMapper.deleteProofByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除凭证信息
     * 
     * @param id 凭证ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProofById(Long id)
    {
        return proofMapper.deleteProofById(id);
    }

    /**
     * 删除凭证对象
     *
     * @param userIds 需要删除的人员ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProofByUserIds(String userIds)
    {
        return proofMapper.deleteProofByUserIds(Convert.toStrArray(userIds));
    }
}
