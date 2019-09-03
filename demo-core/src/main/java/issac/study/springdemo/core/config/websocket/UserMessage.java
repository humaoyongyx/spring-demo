package issac.study.springdemo.core.config.websocket;

import java.io.Serializable;

/**
 * @author humy6
 * 此类会序列化到redis
 */
public class UserMessage implements Serializable {
    /**
     * userId要按照user表中的userId+'@'+tenantId,如1@tenant_!
     */
    private String userId;
    /**
     * 处理的消息类型，可以自己更具传的类型来处理message
     */
    private int type;

    /**
     * 消息
     */
    private String message;

    public UserMessage(){}

    public UserMessage(String userId, int type, String message) {
        this.userId = userId;
        this.type = type;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
