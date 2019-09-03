package issac.study.springdemo.web.controller;

import issac.study.springdemo.core.config.websocket.Message;
import issac.study.springdemo.core.config.websocket.WebSocketTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author humy6
 */
@RequestMapping("redis")
@RestController
public class TestRedisController {

    @Autowired
    WebSocketTemplate webSocketTemplate;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("s1")
    public void testS1(){
        Message message = new Message();
        message.setMessage("hello:"+serverPort);
      //  message.setType(1);
     //   message.setUserIds(Arrays.asList("1@tenant_1"));
        webSocketTemplate.sendMessage(message);
    }

    @RequestMapping("s2")
    public void testS2(){
        Message message = new Message();
        message.setMessage("hello:"+serverPort);
          message.setType(1);
          message.setUserIds(Arrays.asList("1@tenant_1"));
        webSocketTemplate.sendMessage(message);
    }
}
