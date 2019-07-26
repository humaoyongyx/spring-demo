package issac.study.springdemo.service.controller;

import issac.study.springdemo.core.config.message.MessagesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 * @Date: 2019/7/22 8:55
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessagesUtil messagesUtil;

    @RequestMapping
    public String get(){
        return  messagesUtil.get("hello");
    }
}
