package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.core.text.Convert;
import com.snk.common.enums.BusinessType;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.poi.ExcelUtil;
import com.snk.door.domain.Blacklist;
import com.snk.door.domain.Proof;
import com.snk.door.domain.User;
import com.snk.door.service.IBlacklistService;
import com.snk.door.service.IProofService;
import com.snk.door.service.IUserService;
import com.snk.framework.util.ShiroUtils;
import com.snk.system.service.ISysPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人员信息Controller
 * 
 * @author snk
 * @date 2020-08-03
 */
@Controller
@RequestMapping("/door/user")
public class UserController extends BaseController
{
    private String prefix = "door/user";

    @Autowired
    private IUserService userService;
    @Autowired
    private ISysPostService postService;
    @Autowired
    private IBlacklistService blacklistService;
    @Autowired
    private IProofService proofService;

    @RequiresPermissions("door:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    /**
     * 查询人员信息列表
     */
    @RequiresPermissions("door:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(User user)
    {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 导出人员信息列表
     */
    @RequiresPermissions("door:user:export")
    @Log(title = "人员信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(User user)
    {
        List<User> list = userService.selectUserList(user);
        ExcelUtil<User> util = new ExcelUtil<>(User.class);
        return util.exportExcel(list, "人员信息");
    }

    @RequiresPermissions("door:user:import")
    @Log(title = "人员信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<User> util = new ExcelUtil<>(User.class);
        List<User> userList = util.importExcel(file.getInputStream());
        String loginName = ShiroUtils.getSysUser().getLoginName();
        String message = userService.importUser(userList, loginName);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("door:user:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<User> util = new ExcelUtil<>(User.class);
        return util.importTemplateExcel("人员信息");
    }

    /**
     * 新增人员信息
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }

    /**
     * 新增保存人员信息
     */
    @RequiresPermissions("door:user:add")
    @Log(title = "人员信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody User user)
    {
        user.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改人员信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        User user = userService.selectUserById(id);
        mmap.put("user", user);
        mmap.put("posts", postService.selectPostAll());
        List<Proof> proofList = proofService.selectProofList(new Proof(id));
        user.setProofList(proofList);
        return prefix + "/edit";
    }

    /**
     * 修改保存人员信息
     */
    @RequiresPermissions("door:user:edit")
    @Log(title = "人员信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody User user)
    {
        user.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除人员信息
     */
    @RequiresPermissions("door:user:remove")
    @Log(title = "人员信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(userService.deleteUserByIds(ids));
    }

    /**
     * 校验唯一
     */
    @PostMapping("/checkUnique")
    @ResponseBody
    public String checkUnique(User user)
    {
        return userService.checkUnique(user);
    }

    /**
     * 拉黑人员信息
     */
    @RequiresPermissions("door:user:blacklist")
    @Log(title = "人员信息", businessType = BusinessType.BLACKLIST)
    @PostMapping( "/blacklist")
    @ResponseBody
    public AjaxResult blacklist(String ids, Date liftTime, String remark)
    {
        String[] idArray = Convert.toStrArray(ids);
        List<Blacklist> list = new ArrayList<>();
        Blacklist queryParam = new Blacklist();
        queryParam.setRefIds(idArray);
        List<Long> refIds = blacklistService.selectBlacklistList(queryParam).stream().map(Blacklist::getRefId).collect(Collectors.toList());
        for(String id : idArray) {
            if (refIds.contains(Long.parseLong(id))) {
                continue;
            }
            Blacklist blacklist = new Blacklist();
            blacklist.setRefId(Long.parseLong(id));
            blacklist.setLiftTime(liftTime);
            blacklist.setRemark(remark);
            blacklist.setCreateBy(ShiroUtils.getLoginName());
            blacklist.setCreateTime(DateUtils.getNowDate());
            list.add(blacklist);
        }
        return toAjax(blacklistService.insertBlacklistBatch(list));
    }

}
