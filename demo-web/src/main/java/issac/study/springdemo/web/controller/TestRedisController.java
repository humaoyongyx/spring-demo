package issac.study.springdemo.web.controller;

import issac.study.springdemo.web.service.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 */
@RequestMapping("redis")
@RestController
public class TestRedisController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("s1")
    public void testS1(){
        stringRedisTemplate.convertAndSend("web-socket","redis测试"+serverPort);
    }
}
