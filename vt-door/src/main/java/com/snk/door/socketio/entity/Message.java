package com.snk.door.socketio.entity;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    //推送的事件
    public static final String ADD_DEVICE = "add_device";

    public static final String OP_DEVICE = "op_device";

    public static final String WATCH_DEVICE = "watch_device";

    public static final String CUSTOM = "custom";

    public static final String FINGER_READER = "finger_reader";

    private Long userId;

    private String content;

    private String code;

    private byte[] bytes;

    public Message(Long userId) {
        this.userId = userId;
    }

}
