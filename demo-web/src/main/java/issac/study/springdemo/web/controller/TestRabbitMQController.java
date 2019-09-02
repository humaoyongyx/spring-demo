package issac.study.springdemo.web.controller;

import issac.study.springdemo.web.service.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 */
@RequestMapping("mq")
@RestController
public class TestRabbitMQController {

    @Autowired
    Sender sender;

    @RequestMapping("s1")
    public void testS1(){
        sender.send();
    }
}
