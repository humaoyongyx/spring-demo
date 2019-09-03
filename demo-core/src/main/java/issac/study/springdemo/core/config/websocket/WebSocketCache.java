package issac.study.springdemo.core.config.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author humy6
 */
public class WebSocketCache {
    /**
     * 存储session和用户的对应关系
     * 1.WebSocketSession 是不能序列化的，所以是不能存在redis中的
     * 2.可以通过发布订阅关系来实现分布式推送
     */
    private final Map<String, WebSocketSession> sessionCache = new ConcurrentHashMap<>();
    private final Map<String, String> sessionUserCache = new ConcurrentHashMap<>();
    private final Map<String, String> userSessionCache = new ConcurrentHashMap<>();

    private static final WebSocketCache instance = new WebSocketCache();

    private WebSocketCache() {
    }

    public static WebSocketCache getInstance() {
        return instance;
    }

    /**
     * 存储user和session的对应关系
     *
     * @param session
     */
    public void saveSession(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(WebSocketConstants.USER_ID));
        String sessionId = session.getId();
        sessionCache.put(sessionId, session);
        sessionUserCache.put(sessionId, userId);
        userSessionCache.put(userId, sessionId);
    }

    /**
     * 删除user和session的对应关系
     *
     * @param session
     */
    public void removeSession(WebSocketSession session) {
        String sessionId = session.getId();
        sessionCache.remove(sessionId);
        String userId = sessionUserCache.get(sessionId);
        userSessionCache.remove(userId);
        sessionUserCache.remove(sessionId);
    }

    /**
     * 通过userId获取WebSocketSession
     * @param userId
     * @return
     */
    public WebSocketSession getSessionCache(String userId) {
        String sessionId = userSessionCache.get(userId);
        if (sessionId != null) {
            return sessionCache.get(sessionId);
        }
        return null;
    }

}
