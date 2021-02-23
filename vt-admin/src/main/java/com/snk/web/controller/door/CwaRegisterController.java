package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.CwaRegister;
import com.snk.door.service.ICwaRegisterService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 手动登记Controller
 * 
 * @author snk
 * @date 2020-10-28
 */
@Controller
@RequestMapping("/door/cwa/register")
public class CwaRegisterController extends BaseController
{
    private String prefix = "door/cwa/register";

    @Autowired
    private ICwaRegisterService cwaRegisterService;

    @RequiresPermissions("door:register:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/register";
    }

    /**
     * 查询手动登记列表
     */
    @RequiresPermissions("door:register:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CwaRegister cwaRegister)
    {
        startPage();
        List<CwaRegister> list = cwaRegisterService.selectCwaRegisterList(cwaRegister);
        return getDataTable(list);
    }

    /**
     * 新增手动登记
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存手动登记
     */
    @RequiresPermissions("door:register:add")
    @Log(title = "手动登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CwaRegister cwaRegister)
    {
        cwaRegister.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(cwaRegisterService.insertCwaRegister(cwaRegister));
    }

    /**
     * 删除手动登记
     */
    @RequiresPermissions("door:register:remove")
    @Log(title = "手动登记", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cwaRegisterService.deleteCwaRegisterByIds(ids));
    }

}
