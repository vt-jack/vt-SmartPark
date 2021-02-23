package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.Model;
import com.snk.door.service.IModelService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备型号信息Controller
 * 
 * @author snk
 * @date 2020-08-03
 */
@Controller
@RequestMapping("/door/device/model")
public class ModelController extends BaseController
{
    private String prefix = "door/device/model";

    @Autowired
    private IModelService modelService;

    @RequiresPermissions("door:model:view")
    @GetMapping()
    public String model()
    {
        return prefix + "/model";
    }

    /**
     * 查询设备型号信息列表
     */
    @RequiresPermissions("door:model:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Model model)
    {
        startPage();
        List<Model> list = modelService.selectModelList(model);
        return getDataTable(list);
    }

    /**
     * 新增设备型号信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存设备型号信息
     */
    @RequiresPermissions("door:model:add")
    @Log(title = "设备型号信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody Model model)
    {
        model.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(modelService.insertModel(model));
    }

    /**
     * 修改设备型号信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Model model = modelService.selectModelById(id);
        mmap.put("model", model);
        return prefix + "/edit";
    }

    /**
     * 修改保存设备型号信息
     */
    @RequiresPermissions("door:model:edit")
    @Log(title = "设备型号信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody Model model)
    {
        model.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(modelService.updateModel(model));
    }

    /**
     * 删除设备型号信息
     */
    @RequiresPermissions("door:model:remove")
    @Log(title = "设备型号信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(modelService.deleteModelByIds(ids));
    }

    /**
     * 校验唯一
     */
    @PostMapping("/checkUnique")
    @ResponseBody
    public String checkUnique(Model model)
    {
        return modelService.checkUnique(model);
    }

}
