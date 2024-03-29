package issac.study.springdemo.web.bean;

import java.io.Serializable;

/**
 * @author humy6
 */
public class UserMessage implements Serializable {

    private String id;
    private String code;
    private String message;
    private String tenantId;

    public UserMessage(){}
    public UserMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
