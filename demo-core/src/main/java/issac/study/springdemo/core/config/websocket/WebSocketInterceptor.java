package issac.study.springdemo.core.config.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author  hmy
 * 前段将用户id传过来绑定session
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    /**
     * map设置的值可以通过WebSocketSession获取
     * 将前段设置的值存储
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
            String user = request.getServletRequest().getParameter("userId");
            map.put("userId", user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Exception e) {

    }
}