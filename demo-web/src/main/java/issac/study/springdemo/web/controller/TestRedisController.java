package issac.study.springdemo.web.controller;

import issac.study.springdemo.core.config.websocket.UserMessage;
import issac.study.springdemo.core.config.websocket.WebSocketTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        webSocketTemplate.sendMessage(new UserMessage("1@tenant_1",0,"hello World"+serverPort));
    }
}
