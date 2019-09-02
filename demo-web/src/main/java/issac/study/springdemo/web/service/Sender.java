package issac.study.springdemo.web.service;

import issac.study.springdemo.web.bean.UserMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Sender {
 

    @Autowired
    private RabbitTemplate rabbitTemplate;
 
    public void send(){
        String content = "hello" + new Date();
        System.out.println("Sender:" +content);
        rabbitTemplate.convertAndSend(
                "web-exchange", //分发消息的交换机名称
                "",
                new UserMessage("Tony","hi tony"+new Date().toLocaleString())
        );

    }
}
