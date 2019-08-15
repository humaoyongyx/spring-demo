package issac.study.springdemo.web.controller;

import issac.study.springdemo.model.req.PageReq;
import issac.study.springdemo.model.req.UserReq;
import issac.study.springdemo.web.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserFeignClient userFeignClient;

    @RequestMapping("/1")
    public Object test(){
           return userFeignClient.getList(new UserReq(1,"11"),new PageReq(1,2));
    }

    @RequestMapping("/2")
    public Object test2(){
        return userFeignClient.getById(1);
    }
}
