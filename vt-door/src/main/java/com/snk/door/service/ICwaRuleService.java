package com.snk.door.service;

import com.snk.door.domain.CwaRule;
import com.snk.door.domain.User;

import java.util.List;

/**
 * 考勤规则Service接口
 * 
 * @author snk
 * @date 2020-10-28
 */
public interface ICwaRuleService
{
    /**
     * 查询考勤规则
     *
     * @param id 考勤规则ID
     * @return 考勤规则
     */
    CwaRule selectCwaRuleById(Long id);

    /**
     * 查询考勤规则列表
     *
     * @param cwaRule 考勤规则
     * @return 考勤规则集合
     */
    List<CwaRule> selectCwaRuleList(CwaRule cwaRule);

    /**
     * 新增考勤规则
     *
     * @param cwaRule 考勤规则
     * @return 结果
     */
    int insertCwaRule(CwaRule cwaRule);

    /**
     * 修改考勤规则
     *
     * @param cwaRule 考勤规则
     * @return 结果
     */
    int updateCwaRule(CwaRule cwaRule);

    /**
     * 删除考勤规则
     *
     * @param id 考勤规则ID
     * @return 结果
     */
    int deleteCwaRuleById(Long id);

    /**
     * 批量删除考勤规则
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCwaRuleByIds(String ids);

    /**
     * 校验唯一
     * @param cwaRule
     * @return
     */
    String checkUnique(CwaRule cwaRule);

}
