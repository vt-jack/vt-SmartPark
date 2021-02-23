package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.VisitCompany;
import com.snk.door.service.IVisitCompanyService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业信息Controller
 * 
 * @author snk
 * @date 2020-11-18
 */
@Controller
@RequestMapping("/door/visit/company")
public class VisitCompanyController extends BaseController
{
    private String prefix = "door/visit/company";

    @Autowired
    private IVisitCompanyService visitCompanyService;

    @RequiresPermissions("door:visitCompany:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/company";
    }

    /**
     * 查询企业信息列表
     */
    @RequiresPermissions("door:visitCompany:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(VisitCompany visitCompany)
    {
        startPage();
        List<VisitCompany> list = visitCompanyService.selectVisitCompanyList(visitCompany);
        return getDataTable(list);
    }

    /**
     * 查询企业信息列表
     */
    @GetMapping("/autoList")
    @ResponseBody
    public AjaxResult autoList(VisitCompany visitCompany)
    {
        AjaxResult ajax = new AjaxResult();
        ajax.put("code", 200);
        ajax.put("value", visitCompanyService.selectVisitCompanyList(visitCompany));
        return ajax;
    }

    /**
     * 新增企业信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存企业信息
     */
    @RequiresPermissions("door:visitCompany:add")
    @Log(title = "企业信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(VisitCompany visitCompany)
    {
        visitCompany.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(visitCompanyService.insertVisitCompany(visitCompany));
    }

    /**
     * 修改企业信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        VisitCompany visitCompany = visitCompanyService.selectVisitCompanyById(id);
        mmap.put("company", visitCompany);
        return prefix + "/edit";
    }

    /**
     * 修改保存企业信息
     */
    @RequiresPermissions("door:visitCompany:edit")
    @Log(title = "企业信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(VisitCompany visitCompany)
    {
        visitCompany.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(visitCompanyService.updateVisitCompany(visitCompany));
    }

    /**
     * 删除企业信息
     */
    @RequiresPermissions("door:visitCompany:remove")
    @Log(title = "企业信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(visitCompanyService.deleteVisitCompanyByIds(ids));
    }

}
