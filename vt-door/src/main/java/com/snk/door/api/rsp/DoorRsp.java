package com.snk.door.api.rsp;

import com.snk.common.utils.security.PermissionUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class DoorRsp implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID **/
    private Long id;

    /** 设备SN **/
    private String sn;

    /** 用户ID */
    private Long userId;

    /** 用户 */
    private String userName;

    /** 操作结果 */
    private String result;

    /** 参数 */
    private Object param;

    /** 是否发送消息到前端 */
    private Boolean sendMsg;

    public DoorRsp() {
        setUser();
    }

    public DoorRsp(Long id, Object param) {
        this.id = id;
        this.param = param;
        setUser();
    }

    public DoorRsp(String sn, Object param) {
        this.sn = sn;
        this.param = param;
        setUser();
    }

    public DoorRsp(Long id) {
        this.id = id;
        setUser();
    }

    public DoorRsp(String sn) {
        this.sn = sn;
        setUser();
    }

    public void setUser() {
        try {
            this.userId = (Long) PermissionUtils.getPrincipalProperty("userId");
            this.userName = (String) PermissionUtils.getPrincipalProperty("loginName");
        } catch (Exception e) {

        }
    }


}
