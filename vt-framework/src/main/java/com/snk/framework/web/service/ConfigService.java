package com.snk.framework.web.service;

import com.snk.common.config.SocketIOConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snk.system.service.ISysConfigService;

/**
 * snk首创 html调用 thymeleaf 实现参数管理
 * 
 * @author snk
 */
@Service("config")
public class ConfigService
{
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private SocketIOConfig socketIOConfig;

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String getKey(String configKey)
    {
        return configService.selectConfigByKey(configKey);
    }

    /**
     * 获取socket连接的IP地址
     * @return
     */
    public String getSocketIOHost() {
        return socketIOConfig.getPubHost();
    }

    /**
     * 获取socket连接的端口
     * @return
     */
    public Integer getSocketIOPort() {
        return socketIOConfig.getPort();
    }
}
