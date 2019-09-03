package issac.study.springdemo.core.config.websocket.message;

import com.alibaba.fastjson.JSON;
import issac.study.springdemo.core.config.websocket.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author humy6
 * 通过redis的发布订阅，获得的消息
 *
 */
@Component
public class RedisMessageReceiver implements ApplicationContextAware {

    private Logger logger= LoggerFactory.getLogger(RedisMessageReceiver.class);

    private List<IMessageListener> iMessageListeners=new ArrayList<>();

    public void onMessage(String msg) {
        logger.debug("onMessage:{}",msg);
        UserMessage userMessage = JSON.parseObject(msg, UserMessage.class);
        try {
            iMessageListeners.stream().forEach(it->it.onMessage(userMessage));
        }catch (Exception e){
            logger.error("onMessage error:",e);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IMessageListener> beansOfType = applicationContext.getBeansOfType(IMessageListener.class);
        if (beansOfType != null){
            for (String beanName:beansOfType.keySet()){
                IMessageListener iMessageListener = (IMessageListener) applicationContext.getBean(beanName);
                iMessageListeners.add(iMessageListener);
            }
        }
    }
}
