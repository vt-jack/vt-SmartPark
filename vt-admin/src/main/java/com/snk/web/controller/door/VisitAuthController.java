package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.constant.Constants;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.TimeSlot;
import com.snk.door.domain.VisitAuth;
import com.snk.door.service.ITimeSlotService;
import com.snk.door.service.IVisitAuthService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 访问权限Controller
 * 
 * @author snk
 * @date 2020-11-18
 */
@Controller
@RequestMapping("/door/visit/auth")
public class VisitAuthController extends BaseController
{
    private String prefix = "door/visit/auth";

    @Autowired
    private IVisitAuthService visitAuthService;
    @Autowired
    private ITimeSlotService timeSlotService;

    @RequiresPermissions("door:visitAuth:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/auth";
    }

    /**
     * 查询访问权限列表
     */
    @RequiresPermissions("door:visitAuth:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(VisitAuth visitAuth)
    {
        startPage();
        List<VisitAuth> list = visitAuthService.selectVisitAuthList(visitAuth);
        return getDataTable(list);
    }

    /**
     * 查询访问权限列表
     */
    @GetMapping("/autoList")
    @ResponseBody
    public AjaxResult autoList(VisitAuth visitAuth)
    {
        AjaxResult ajax = new AjaxResult();
        ajax.put("code", 200);
        ajax.put("value", visitAuthService.selectVisitAuthList(visitAuth));
        return ajax;
    }

    /**
     * 新增访问权限
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("times", timeSlotService.selectTimeSlotListByUserId(ShiroUtils.getUserId()));
        return prefix + "/add";
    }

    /**
     * 新增保存访问权限
     */
    @RequiresPermissions("door:visitAuth:add")
    @Log(title = "访问权限", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(VisitAuth visitAuth)
    {
        visitAuth.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(visitAuthService.insertVisitAuth(visitAuth));
    }

    /**
     * 修改访问权限
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        VisitAuth visitAuth = visitAuthService.selectVisitAuthById(id);
        mmap.put("auth", visitAuth);
        mmap.put("times", timeSlotService.selectTimeSlotListByUserId(ShiroUtils.getUserId()));
        return prefix + "/edit";
    }

    /**
     * 修改保存访问权限
     */
    @RequiresPermissions("door:visitAuth:edit")
    @Log(title = "访问权限", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(VisitAuth visitAuth)
    {
        visitAuth.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(visitAuthService.updateVisitAuth(visitAuth));
    }

    /**
     * 删除访问权限
     */
    @RequiresPermissions("door:visitAuth:remove")
    @Log(title = "访问权限", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(visitAuthService.deleteVisitAuthByIds(ids));
    }

}
