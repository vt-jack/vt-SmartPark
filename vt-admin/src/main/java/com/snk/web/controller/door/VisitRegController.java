package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.VisitReg;
import com.snk.door.service.IVisitRegService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 来访登记Controller
 *
 * @author snk
 * @date 2020-11-18
 */
@Controller
@RequestMapping("/door/visit/reg")
public class VisitRegController extends BaseController
{
    private String prefix = "door/visit/reg";

    @Autowired
    private IVisitRegService visitRegService;

    @RequiresPermissions("door:visitReg:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/reg";
    }

    /**
     * 查询来访登记列表
     */
    @RequiresPermissions("door:visitReg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(VisitReg visitReg)
    {
        startPage();
        List<VisitReg> list = visitRegService.selectVisitRegList(visitReg);
        return getDataTable(list);
    }

    /**
     * 根据卡号或手机号查询登记信息
     */
    @GetMapping("/listBySearchValue")
    @ResponseBody
    public List<VisitReg> listBySearchValue(VisitReg visitReg)
    {
        return visitRegService.selectBySearchValue(visitReg);
    }

    /**
     * 新增来访登记
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存来访登记
     */
    @RequiresPermissions("door:visitReg:add")
    @Log(title = "来访登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(VisitReg visitReg)
    {
        visitReg.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(visitRegService.insertVisitReg(visitReg));
    }

    /**
     * 注销
     */
    @RequiresPermissions("door:visitReg:cancel")
    @Log(title = "来访登记", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{id}")
    @ResponseBody
    public AjaxResult cancel(@PathVariable("id") Long id)
    {
        visitRegService.cancel(id);
        return AjaxResult.success();
    }

    /**
     * 删除来访登记
     */
    @RequiresPermissions("door:visitReg:remove")
    @Log(title = "来访登记", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(visitRegService.deleteVisitRegByIds(ids));
    }

    /**
     * 校验唯一
     */
    @PostMapping("/checkUnique")
    @ResponseBody
    public String checkUnique(VisitReg visitReg)
    {
        return visitRegService.checkUnique(visitReg);
    }
}
