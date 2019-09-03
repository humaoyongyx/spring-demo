package issac.study.springdemo.core.config.websocket;

import issac.study.springdemo.core.config.websocket.message.RedisMessageReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author hmy
 */
@Configuration
@EnableWebSocket
public class WebSocketAutoConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // webSocket通道
        // 指定处理器和路径
        registry.addHandler(new WebSocketHandler(), WebSocketConstants.WEBSOCKET_ENDPOINT)
                // 指定自定义拦截器
                .addInterceptors(new WebSocketInterceptor())
                // 允许跨域
                .setAllowedOrigins("*");
        // sockJs通道
        registry.addHandler(new WebSocketHandler(), WebSocketConstants.SOCKET_JS_ENDPOINT)
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*")
                // 开启sockJs支持
                .withSockJS();
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //redis订阅一个通道
        container.addMessageListener(listenerAdapter, new PatternTopic(WebSocketConstants.REDIS_CHANEL));
        return container;
    }

    /**
     * 注入RedisMessageReceiver并且消息回掉METHOD_ON_MESSAGE方法
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, WebSocketConstants.METHOD_ON_MESSAGE);
    }
}