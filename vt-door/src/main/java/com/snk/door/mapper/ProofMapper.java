package com.snk.door.mapper;

import com.snk.door.domain.Blacklist;
import com.snk.door.domain.Proof;

import java.util.List;

/**
 * 凭证Mapper接口
 * 
 * @author snk
 * @date 2020-08-06
 */
public interface ProofMapper
{
    /**
     * 查询凭证
     * 
     * @param id 凭证ID
     * @return 凭证
     */
    Proof selectProofById(Long id);

    /**
     * 查询凭证列表
     *
     * @param userId 人员ID
     * @return 凭证集合
     */
    List<Proof> selectProofByUserId(Long userId);

    /**
     * 查询凭证列表
     * 
     * @param proof 凭证
     * @return 凭证集合
     */
    List<Proof> selectProofList(Proof proof);

    /**
     * 新增凭证
     * 
     * @param proof 凭证
     * @return 结果
     */
    int insertProof(Proof proof);

    /**
     * 批量新增凭证
     * @param list
     * @return
     */
    int insertProofBatch(List<Proof> list);

    /**
     * 修改凭证
     * 
     * @param proof 凭证
     * @return 结果
     */
    int updateProof(Proof proof);

    /**
     * 批量修改凭证
     *
     * @param list 凭证
     * @return 结果
     */
    int updateProofBatch(List<Proof> list);

    /**
     * 删除凭证
     * 
     * @param id 凭证ID
     * @return 结果
     */
    int deleteProofById(Long id);

    /**
     * 批量删除凭证
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteProofByIds(String[] ids);

    /**
     * 批量删除凭证
     *
     * @param userIds 需要删除的人员ID
     * @return 结果
     */
    int deleteProofByUserIds(String[] userIds);
}
