package issac.study.springdemo.service.controller;

import issac.study.springdemo.service.req.SimpleReq;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author humy6
 * @Date: 2019/8/7 9:26
 */
@RestController
@RequestMapping("valid")
public class ValidatorController {

    @RequestMapping("/1")
    public void test(@Valid SimpleReq simpleReq){
        System.out.println(simpleReq);
    }
}
