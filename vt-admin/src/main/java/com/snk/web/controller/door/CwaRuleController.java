package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.CwaRule;
import com.snk.door.service.ICwaRuleService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考勤规则Controller
 * 
 * @author snk
 * @date 2020-08-03
 */
@Controller
@RequestMapping("/door/cwa/rule")
public class CwaRuleController extends BaseController
{
    private String prefix = "door/cwa/rule";

    @Autowired
    private ICwaRuleService cwaRuleService;

    @RequiresPermissions("door:rule:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/rule";
    }

    /**
     * 查询考勤规则列表
     */
    @RequiresPermissions("door:rule:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CwaRule cwaRule)
    {
        startPage();
        List<CwaRule> list = cwaRuleService.selectCwaRuleList(cwaRule);
        return getDataTable(list);
    }

    /**
     * 新增考勤规则
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考勤规则
     */
    @RequiresPermissions("door:rule:add")
    @Log(title = "考勤规则", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CwaRule cwaRule)
    {
        cwaRule.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(cwaRuleService.insertCwaRule(cwaRule));
    }

    /**
     * 修改考勤规则
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CwaRule cwaRule = cwaRuleService.selectCwaRuleById(id);
        mmap.put("rule", cwaRule);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤规则
     */
    @RequiresPermissions("door:rule:edit")
    @Log(title = "考勤规则", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CwaRule cwaRule)
    {
        cwaRule.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(cwaRuleService.updateCwaRule(cwaRule));
    }

    /**
     * 删除考勤规则
     */
    @RequiresPermissions("door:rule:remove")
    @Log(title = "考勤规则", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cwaRuleService.deleteCwaRuleByIds(ids));
    }

    /**
     * 校验唯一
     */
    @PostMapping("/checkUnique")
    @ResponseBody
    public String checkUnique(CwaRule cwaRule)
    {
        return cwaRuleService.checkUnique(cwaRule);
    }

}
