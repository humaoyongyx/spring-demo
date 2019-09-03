package issac.study.springdemo.core.config.websocket.message;

import issac.study.springdemo.core.config.websocket.UserMessage;

/**
 * @author humy6
 * 实现此接口，且加入spring容器，将会收到监听的消息
 */
public interface IMessageListener {
    /**
     * 消息监听接口
     * @param message
     */
    void onMessage(UserMessage message);
}
