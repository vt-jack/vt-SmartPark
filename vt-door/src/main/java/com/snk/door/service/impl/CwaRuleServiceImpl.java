package com.snk.door.service.impl;

import com.snk.common.constant.UserConstants;
import com.snk.common.core.text.Convert;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.domain.CwaRule;
import com.snk.door.mapper.CwaRuleMapper;
import com.snk.door.service.ICwaRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 考勤规则Service业务层处理
 * 
 * @author snk
 * @date 2020-10-28
 */
@Service
public class CwaRuleServiceImpl implements ICwaRuleService
{
    private static final Logger log = LoggerFactory.getLogger(CwaRuleServiceImpl.class);

    @Autowired
    private CwaRuleMapper cwaRuleMapper;

    /**
     * 查询考勤规则
     *
     * @param id 考勤规则ID
     * @return 考勤规则
     */
    @Override
    public CwaRule selectCwaRuleById(Long id) {
        return cwaRuleMapper.selectCwaRuleById(id);
    }

    /**
     * 查询考勤规则列表
     *
     * @param cwaRule 考勤规则
     * @return 考勤规则集合
     */
    @Override
    public List<CwaRule> selectCwaRuleList(CwaRule cwaRule) {
        return cwaRuleMapper.selectCwaRuleList(cwaRule);
    }

    /**
     * 新增考勤规则
     *
     * @param cwaRule 考勤规则
     * @return 结果
     */
    @Override
    public int insertCwaRule(CwaRule cwaRule) {
        return cwaRuleMapper.insertCwaRule(cwaRule);
    }

    /**
     * 修改考勤规则
     *
     * @param cwaRule 考勤规则
     * @return 结果
     */
    @Override
    public int updateCwaRule(CwaRule cwaRule) {
        return cwaRuleMapper.updateCwaRule(cwaRule);
    }

    /**
     * 删除考勤规则
     *
     * @param id 考勤规则ID
     * @return 结果
     */
    @Override
    public int deleteCwaRuleById(Long id) {
        return cwaRuleMapper.deleteCwaRuleById(id);
    }

    /**
     * 批量删除考勤规则
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCwaRuleByIds(String ids) {
        return cwaRuleMapper.deleteCwaRuleByIds(Convert.toStrArray(ids));
    }

    /**
     * 校验唯一
     *
     * @param cwaRule 用户信息
     * @return
     */
    @Override
    public String checkUnique(CwaRule cwaRule)
    {
        Long ruleId = StringUtils.isNull(cwaRule.getId()) ? -1L : cwaRule.getId();
        List<CwaRule> ruleList = cwaRuleMapper.selectCwaRuleList(cwaRule);
        if (!CollectionUtils.isEmpty(ruleList) && ruleList.get(0).getId().longValue() != ruleId.longValue())
        {
            return UserConstants.EXCEPTION;
        }
        return UserConstants.NORMAL;
    }

}
