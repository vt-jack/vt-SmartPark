package com.snk.web.controller.door;

import java.util.List;

import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snk.common.annotation.Log;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.Blacklist;
import com.snk.door.service.IBlacklistService;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.utils.poi.ExcelUtil;
import com.snk.common.core.page.TableDataInfo;

/**
 * 黑名单Controller
 * 
 * @author snk
 * @date 2020-08-06
 */
@Controller
@RequestMapping("/door/user/blacklist")
public class BlacklistController extends BaseController
{
    private String prefix = "door/user/blacklist";

    @Autowired
    private IBlacklistService blacklistService;

    @RequiresPermissions("door:blacklist:view")
    @GetMapping()
    public String blacklist()
    {
        return prefix + "/blacklist";
    }

    /**
     * 查询黑名单列表
     */
    @RequiresPermissions("door:blacklist:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Blacklist blacklist)
    {
        startPage();
        List<Blacklist> list = blacklistService.selectBlacklistList(blacklist);
        return getDataTable(list);
    }

    /**
     * 导出黑名单列表
     */
    @RequiresPermissions("door:blacklist:export")
    @Log(title = "黑名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Blacklist blacklist)
    {
        List<Blacklist> list = blacklistService.selectBlacklistList(blacklist);
        ExcelUtil<Blacklist> util = new ExcelUtil<Blacklist>(Blacklist.class);
        return util.exportExcel(list, "blacklist");
    }

    /**
     * 新增黑名单
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存黑名单
     */
    @RequiresPermissions("door:blacklist:add")
    @Log(title = "黑名单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Blacklist blacklist)
    {
        blacklist.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(blacklistService.insertBlacklist(blacklist));
    }

    /**
     * 修改黑名单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Blacklist blacklist = blacklistService.selectBlacklistById(id);
        mmap.put("blacklist", blacklist);
        return prefix + "/edit";
    }

    /**
     * 修改保存黑名单
     */
    @RequiresPermissions("door:blacklist:edit")
    @Log(title = "黑名单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Blacklist blacklist)
    {
        blacklist.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(blacklistService.updateBlacklist(blacklist));
    }

    /**
     * 删除黑名单
     */
    @RequiresPermissions("door:blacklist:remove")
    @Log(title = "黑名单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(blacklistService.deleteBlacklistByIds(ids));
    }
}
