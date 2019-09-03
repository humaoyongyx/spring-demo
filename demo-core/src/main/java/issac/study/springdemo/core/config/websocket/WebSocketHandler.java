package issac.study.springdemo.core.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @author hmy
 */
public class WebSocketHandler extends AbstractWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    private WebSocketCache webSocketCache = WebSocketCache.getInstance();

    /**
     * webSocket连接创建后调用
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketCache.saveSession(session);
    }

    /**
     * 接收到消息会调用
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            logger.info("message:{}", message);
        } else if (message instanceof BinaryMessage) {

        } else if (message instanceof PongMessage) {

        } else {
            logger.error("Unexpected WebSocket message type:{}", message);
        }
    }

    /**
     * 连接出错会调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        webSocketCache.removeSession(session);
    }

    /**
     * 连接关闭会调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketCache.removeSession(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}