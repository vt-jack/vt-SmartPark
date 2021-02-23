package com.snk.system.utils;

import com.snk.common.constant.SymbolConstants;
import com.snk.system.domain.SysPost;
import com.snk.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PostUtils {

    @Autowired
    private ISysPostService service;
    private static ISysPostService sysPostService;

    @PostConstruct
    public void init() {
        sysPostService = service;
    }

    public static String selectPostList() {
        List<SysPost> postList = sysPostService.selectPostAll();
        StringBuffer sb = new StringBuffer();
        for (SysPost item : postList) {
            sb.append(SymbolConstants.COMMA).append(item.getPostName());
        }
        return sb.substring(1);
    }
}
