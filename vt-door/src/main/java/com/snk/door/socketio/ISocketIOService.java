package com.snk.door.socketio;

import com.snk.door.socketio.entity.Message;

public interface ISocketIOService {

    // 启动服务
    void start() throws Exception;

    // 停止服务
    void stop();

    // 推送信息
    void pushMessage(String event, Message message);
}
