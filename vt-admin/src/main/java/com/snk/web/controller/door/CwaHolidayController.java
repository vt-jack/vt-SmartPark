package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.CwaHoliday;
import com.snk.door.service.ICwaHolidayService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 节假日Controller
 * 
 * @author snk
 * @date 2020-10-28
 */
@Controller
@RequestMapping("/door/cwa/holiday")
public class CwaHolidayController extends BaseController
{
    private String prefix = "door/cwa/holiday";

    @Autowired
    private ICwaHolidayService cwaHolidayService;

    @RequiresPermissions("door:holiday:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/holiday";
    }

    /**
     * 查询节假日列表
     */
    @RequiresPermissions("door:holiday:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CwaHoliday cwaHoliday)
    {
        startPage();
        List<CwaHoliday> list = cwaHolidayService.selectCwaHolidayList(cwaHoliday);
        return getDataTable(list);
    }

    /**
     * 查询节假日列表
     */
    @GetMapping("/all")
    @ResponseBody
    public List<CwaHoliday> all()
    {
        return cwaHolidayService.selectCwaHolidayList(new CwaHoliday());
    }

    /**
     * 新增节假日
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存节假日
     */
    @RequiresPermissions("door:holiday:add")
    @Log(title = "节假日", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CwaHoliday cwaHoliday)
    {
        cwaHoliday.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(cwaHolidayService.insertCwaHoliday(cwaHoliday));
    }

    /**
     * 修改节假日
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, ModelMap mmap)
    {
        mmap.put("holiday", cwaHolidayService.selectCwaHolidayById(id));
        return prefix + "/edit";
    }

    /**
     * 修改保存节假日
     */
    @RequiresPermissions("door:holiday:edit")
    @Log(title = "节假日", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CwaHoliday cwaHoliday)
    {
        cwaHoliday.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(cwaHolidayService.updateCwaHoliday(cwaHoliday));
    }

    /**
     * 删除节假日
     */
    @RequiresPermissions("door:holiday:remove")
    @Log(title = "节假日", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cwaHolidayService.deleteCwaHolidayByIds(ids));
    }

}
