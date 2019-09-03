package issac.study.springdemo.core.config.websocket;

import java.io.Serializable;
import java.util.List;

/**
 * @author humy6
 * 此类会序列化到redis
 */
public class Message implements Serializable {
    /**
     * userId要按照user表中的userId+'@'+tenantId,如1@tenant_!
     */
    private List<String> userIds;

    /**
     * 处理的消息类型，0 发送给所有人 1 发送给用户
     */
    private int type;

    /**
     * 消息
     */
    private String message;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
