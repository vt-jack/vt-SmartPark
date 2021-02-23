package com.snk.web.controller.api;

import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.control.write.WriteDeadlineService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 人脸接口
 * 
 * @author snk
 */
@Api("人脸接口")
@RestController
@RequestMapping("/api/face")
public class FaceController extends BaseController
{

    private static final Logger log = LoggerFactory.getLogger(FaceController.class);

    @PostMapping()
    public AjaxResult heart(HttpServletRequest request, HttpServletResponse response)
    {
        log.info("心跳.....................");
        return AjaxResult.success();
    }

}
