package com.snk.door.socketio.impl;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.snk.common.core.text.Convert;
//import com.snk.door.api.finger.yz.FingerReaderYzService;
import com.snk.door.mongo.entity.CurrentRecord;
import com.snk.door.socketio.ISocketIOService;
import com.snk.door.socketio.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketIOServiceImpl implements ISocketIOService {

    private static final Logger log = LoggerFactory.getLogger(SocketIOServiceImpl.class);

    // 用来存已连接的客户端
    private static Map<Long, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;
    /*@Autowired
    private FingerReaderYzService fingerReaderYzService;*/

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     * @throws Exception
     */
    @PreDestroy
    private void autoStop()  {
        stop();
    }

    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            Long userId = getUserIdByClient(client);
            if (userId != null) {
                clientMap.put(userId, client);  // 将userI与监听对象放入map
            }
            log.info("客户端连接成功，用户ID：{}", userId);
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            Long userId = getUserIdByClient(client);
            if (!ObjectUtils.isEmpty(userId)) {
                clientMap.remove(userId);   // 将userId移除监听map
                client.disconnect();
            }
            /*String event = getEventByClient(client);
            if (Message.FINGER_READER.equals(event)) {
                fingerReaderYzService.close();
            }*/
            log.info("客户端断开连接，用户ID：{}", userId);
        });

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener(Message.ADD_DEVICE, Message.class, (client, data, ackSender) -> {

        });

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener(Message.OP_DEVICE, Message.class, (client, data, ackSender) -> {

        });
        socketIOServer.start();
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void pushMessage(String event, Message message) {
        if (Message.WATCH_DEVICE.equals(event)) {   // 数据监控
            for (Long key : clientMap.keySet()) {   // 群发
                SocketIOClient client = clientMap.get(key);
                if (!Message.WATCH_DEVICE.equals(getEventByClient(client))) {
                    continue;
                }
                if (client != null && isPush(client, message)) {
                    client.sendEvent(event, message);
                }
            }
        } else {
            Long userId = message.getUserId();
            if (!ObjectUtils.isEmpty(userId)) {
                SocketIOClient client = clientMap.get(userId);
                if (client != null) {
                    client.sendEvent(event, message);
                }
            }
        }
    }

    /**
     * 是否推送
     * @param client
     * @return
     */
    private boolean isPush(SocketIOClient client, Message message) {
        CurrentRecord record = JSON.parseObject(message.getContent(), CurrentRecord.class);
        if (!ObjectUtils.isEmpty(record.getDeviceId())) {
            String ids = getIdsByClient(client);
            if (StringUtils.isEmpty(ids)) {
                return false;
            }
            List<String> idList = Arrays.asList(Convert.toStrArray(ids));
            if (idList.contains(record.getDeviceId().toString())) {
                return true;
            }
        } else {
            String sns = getSnsByClient(client);
            if (StringUtils.isEmpty(sns)) {
                return false;
            }
            List<String> snList = Arrays.asList(Convert.toStrArray(sns));
            if (snList.contains(record.getSn())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private Long getUserIdByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的userId必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("userId");
        if (list != null && list.size() > 0) {
            return Long.valueOf(list.get(0));
        }
        return null;
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private String getEventByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的event必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("event");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private String getIdsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的ids必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("ids");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private String getSnsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的sns必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("sns");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
