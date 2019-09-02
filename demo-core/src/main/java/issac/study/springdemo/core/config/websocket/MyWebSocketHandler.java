package issac.study.springdemo.core.config.websocket;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler extends AbstractWebSocketHandler {


    private static  Logger logger= LoggerFactory.getLogger(MyWebSocketHandler.class);
    /**
    *  存储sessionId和webSocketSession
    *  需要注意的是，webSocketSession没有提供无参构造，不能进行序列化，也就不能通过redis存储
    *  在分布式系统中，要想别的办法实现webSocketSession共享
    */
    public static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    public static Map<String, String> userMap = new ConcurrentHashMap<>();



    private static  void printSession(){
        logger.info(JSON.toJSONString(sessionMap.keySet()));
        logger.info(JSON.toJSONString(userMap.keySet()));
    }
    /**
     * webSocket连接创建后调用
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
         // 获取参数
        String user = String.valueOf(session.getAttributes().get("user"));
        userMap.put(user, session.getId());
        sessionMap.put(session.getId(), session);
        System.out.println("im Established...");
        logger.info("im Established...");
        printSession();
    }

    /**
     * 接收到消息会调用
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            System.out.println(message);
            logger.info("message:{}",message);
            printSession();
        } else if (message instanceof BinaryMessage) {
            
        } else if (message instanceof PongMessage) {
           
        } else {
            System.out.println("Unexpected WebSocket message type: " + message);
        }
    }

    /**
     * 连接出错会调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        sessionMap.remove(session.getId());
        System.out.println("im wrong...");
        logger.info("im wrong...");
        printSession();
    }

    /**
     * 连接关闭会调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());
        System.out.println("im closed...");
        logger.info("im closed...");
        printSession();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
    * 后端发送消息
    */
    public static void sendMessage(String user, String message) throws IOException {
        String sessionId = userMap.get(user);
        if (sessionId==null){
            return;
        }
        WebSocketSession session = sessionMap.get(sessionId);

        if (session !=null && session.isOpen()){
            session.sendMessage(new TextMessage(message));
            System.out.println("im send...");
        }else {
            System.out.println("im none");
        }

    }
}