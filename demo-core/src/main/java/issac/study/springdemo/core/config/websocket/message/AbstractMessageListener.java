package issac.study.springdemo.core.config.websocket.message;

import issac.study.springdemo.core.config.websocket.Message;
import issac.study.springdemo.core.config.websocket.WebSocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author humy6
 */
public abstract class AbstractMessageListener implements IMessageListener {

    private Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class);

    @Override
    public void onMessage(Message message) {
        int type = message.getType();
        //发送给所有人
        if (type == 0) {
            try {
                WebSocketUtils.sendMessageToAll(message.getMessage());
            } catch (Exception e) {
                logger.error("onMessage error", e);
            }
            //发送给指定列表的人
        } else if (type == 1) {
            try {
                WebSocketUtils.sendMessage(message.getUserIds(), message.getMessage());
            } catch (Exception e) {
                logger.error("onMessage error", e);
            }
        }
    }

}
