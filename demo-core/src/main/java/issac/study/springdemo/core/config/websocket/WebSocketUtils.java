package issac.study.springdemo.core.config.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author humy6
 */
public class WebSocketUtils {

    private static WebSocketCache webSocketCache=WebSocketCache.getInstance();

    /**
     * 向websocket发送消息
     */
    public static void sendMessage(String userId, String message) throws IOException {
        WebSocketSession webSocketSession = webSocketCache.getSessionCache(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            webSocketSession.sendMessage(new TextMessage(message));
        }
    }
}
