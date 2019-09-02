package issac.study.springdemo.web.service;

import issac.study.springdemo.core.config.websocket.MyWebSocketHandler;
import issac.study.springdemo.web.bean.UserMessage;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue,
        exchange = @Exchange(value = "web-exchange",durable = "true",type = ExchangeTypes.FANOUT)
))
public class Receiver {
    @Value("${server.port}")
    private String serverPort;

    @RabbitHandler
    public void receiveSocket(@Payload UserMessage userMessage) throws Exception{
        System.out.println("消息内容："+userMessage);
        try {
            MyWebSocketHandler.sendMessage("Tony",userMessage.getMessage()+",port："+serverPort);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
