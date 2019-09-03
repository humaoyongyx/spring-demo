package issac.study.springdemo.core.config.websocket;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author humy6
 */
@Component
public class WebSocketTemplate {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

     public void sendMessage(Message message)  {
         stringRedisTemplate.convertAndSend(WebSocketConstants.REDIS_CHANEL, JSON.toJSONString(message));
     }

}
