package issac.study.springdemo.core.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author humy6
 *
 * 此类是向本地建立的websocket session发送消息的类
 */
public final class WebSocketUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketUtils.class);

    private static WebSocketCache webSocketCache = WebSocketCache.getInstance();
    /**
     * userMessageBlockingQueue 是向具体用户发送消息的处理队列
     * messageBlockingQueue 群发的队列
     */
    private static BlockingQueue<UserMessage> userMessageBlockingQueue = new LinkedBlockingQueue();
    private static BlockingQueue<UserMessage> messageBlockingQueue = new LinkedBlockingQueue();

    private WebSocketUtils() {
    }

    public static void init(){
        new Thread(() -> {
            try {
                takeUserMessage();
            } catch (InterruptedException e) {
                LOGGER.error("takeUserMessage error", e);
            }
        }).start();

        new Thread(() -> {
            try {
                takeMessage();
            } catch (InterruptedException e) {
                LOGGER.error("takeMessage error", e);
            }
        }).start();
    }

    /**
     * 向websocket发送消息
     *
     * @param userId  如1@tenant_1
     * @param message
     * @throws IOException
     */
    public static void sendMessage(String userId, String message) throws IOException, InterruptedException {
        UserMessage userMessage = new UserMessage(userId, null, message);
        userMessageBlockingQueue.put(userMessage);
    }

    /**
     * 向websocket发送消息
     *
     * @param userIds 列表 如1@tenant_1
     * @param message
     * @throws IOException
     * @throws InterruptedException
     */
    public static void sendMessage(List<String> userIds, String message) throws IOException, InterruptedException {
        for (String userId : userIds) {
            UserMessage userMessage = new UserMessage(userId, null, message);
            userMessageBlockingQueue.put(userMessage);
        }
    }

    /**
     * 向所有session发送消息
     *
     * @param message
     * @throws IOException
     * @throws InterruptedException
     */
    public static void sendMessageToAll(String message) throws IOException, InterruptedException {
        Collection<WebSocketSession> allSessionCache = webSocketCache.getAllSessionCache();
        for (WebSocketSession webSocketSession : allSessionCache) {
            UserMessage userMessage = new UserMessage(null, webSocketSession.getId(), message);
            messageBlockingQueue.put(userMessage);
        }
    }

    private static void sendMessage0(String userId, String message) throws IOException {
        WebSocketSession webSocketSession = webSocketCache.getSessionCache(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            webSocketSession.sendMessage(new TextMessage(message));
        }
    }

    /**
     * 向websocket发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException
     */
    private static void sendMessageBySessionId0(String sessionId, String message) throws IOException {
        WebSocketSession webSocketSession = webSocketCache.getSessionCacheBySessionId(sessionId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            webSocketSession.sendMessage(new TextMessage(message));
        }
    }


    private static void takeUserMessage() throws InterruptedException {
        while (true) {
            UserMessage userMessage = userMessageBlockingQueue.take();
            try {
                WebSocketUtils.sendMessage0(userMessage.getUserId(), userMessage.getMessage());
            } catch (IOException e) {
                LOGGER.error("AbstractMessageListener onMessage error", e);
            }
        }
    }

    private static void takeMessage() throws InterruptedException {
        while (true) {
            UserMessage userMessage = messageBlockingQueue.take();
            try {
                WebSocketUtils.sendMessageBySessionId0(userMessage.getSessionId(), userMessage.getMessage());
            } catch (IOException e) {
                LOGGER.error("AbstractMessageListener onMessage error", e);
            }
        }
    }
}
