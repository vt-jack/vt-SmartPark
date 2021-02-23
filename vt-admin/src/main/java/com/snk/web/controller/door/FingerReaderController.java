/*
package com.snk.web.controller.door;

import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.door.api.finger.yz.FingerReaderYzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

*/
/**
 * 人员信息Controller
 * 
 * @author snk
 * @date 2020-08-03
 *//*

@Controller
@RequestMapping("/door/finger")
public class FingerReaderController extends BaseController
{

    private String prefix = "door/finger";

    @Autowired
    private FingerReaderYzService fingerReaderYzService;

    */
/**
     * 打开指纹仪
     *//*

    @GetMapping("/reader")
    public String reader()
    {
        return prefix + "/reader";
    }

    */
/**
     * 关闭指纹仪
     *//*

    @PostMapping("/connect")
    @ResponseBody
    public AjaxResult connect()
    {
        fingerReaderYzService.registrationProcess();
        return AjaxResult.success();
    }

}
*/
