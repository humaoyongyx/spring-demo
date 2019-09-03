package issac.study.springdemo.core.config.websocket.message;

import issac.study.springdemo.core.config.websocket.UserMessage;
import issac.study.springdemo.core.config.websocket.WebSocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author humy6
 */
public abstract  class AbstractMessageListener implements IMessageListener {

    private Logger logger= LoggerFactory.getLogger(AbstractMessageListener.class);

    @Override
    public void onMessage(UserMessage message) {
        try {
            WebSocketUtils.sendMessage(message.getUserId(),message.getMessage());
        } catch (IOException e) {
            logger.error("AbstractMessageListener onMessage error",e);
        }
    }
}
