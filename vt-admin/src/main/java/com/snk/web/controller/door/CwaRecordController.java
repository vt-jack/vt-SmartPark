package com.snk.web.controller.door;

import com.alibaba.fastjson.JSON;
import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.common.utils.poi.ExcelUtil;
import com.snk.door.domain.CwaRecord;
import com.snk.door.service.ICwaRecordService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 考勤报表Controller
 * 
 * @author snk
 * @date 2020-10-28
 */
@Controller
@RequestMapping("/door/cwa/record")
public class CwaRecordController extends BaseController
{
    private String prefix = "door/cwa/record";

    @Autowired
    private ICwaRecordService cwaRecordService;

    @RequiresPermissions("door:cwaRecord:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/record";
    }

    /**
     * 查询考勤报表列表
     */
    @RequiresPermissions("door:cwaRecord:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CwaRecord cwaRecord)
    {
        startPage();
        List<CwaRecord> list = cwaRecordService.selectCwaRecordList(cwaRecord);
        return getDataTable(list);
    }

    /**
     * 导出考勤报表
     */
    @RequiresPermissions("door:cwaRecord:export")
    @Log(title = "考勤报表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CwaRecord cwaRecord)
    {
        List<CwaRecord> list = cwaRecordService.selectCwaRecordList(cwaRecord);
        list.stream().forEach(item -> {
            String itemJson = "";
            List<Map> cwaList = JSON.parseObject(item.getItemJson(), ArrayList.class);
            for (int i = 0; i < cwaList.size(); i++) {
                Map map = cwaList.get(i);
                itemJson += item.getYear() + "-" + item.getMonth() + "-" + (i < 9 ? "0" : "") + (i + 1);
                itemJson += " 上班打卡：" + map.get("sw") + " 下班打卡：" + map.get("dw");
                List<String> remark = (List) map.get("remark");
                if (CollectionUtils.isEmpty(remark)) {
                    continue;
                }
                itemJson += " 备注【";
                for (String rm : remark) {
                    itemJson += rm.replace("F", "法定节假日").replace("X", "休息日")
                            .replace("C", "迟到").replace("Z", "早退")
                            .replace("A", "缺勤").replace("Q", "请假")
                            .replace("B", "补卡").replace("T", "调休")
                            .replace("K", "旷工");
                }
                itemJson += "】";
                if (i != cwaList.size()) {
                    itemJson += "\n";
                }
            }
            item.setItemJson(itemJson);
        });
        ExcelUtil<CwaRecord> util = new ExcelUtil<>(CwaRecord.class);
        return util.exportExcel(list, "考勤报表");
    }

    /**
     * 生成考勤报表
     */
    @RequiresPermissions("door:cwaRecord:init")
    @Log(title = "考勤报表", businessType = BusinessType.INSERT)
    @PostMapping("/init")
    @ResponseBody
    public AjaxResult addSave(CwaRecord cwaRecord)
    {
        cwaRecord.setCreateBy(ShiroUtils.getLoginName());
        cwaRecordService.initCwaRecord(cwaRecord);
        return AjaxResult.success();
    }

    /**
     * 考勤详情
     */
    @RequiresPermissions("door:cwaRecord:detail")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        CwaRecord cwaRecord = cwaRecordService.selectCwaRecordById(id);
        mmap.put("record", cwaRecord);
        return prefix + "/detail";
    }

}
