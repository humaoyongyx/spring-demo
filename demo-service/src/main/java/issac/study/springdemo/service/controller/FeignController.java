package issac.study.springdemo.service.controller;

import com.alibaba.fastjson.JSON;
import issac.study.springdemo.model.req.PageReq;
import issac.study.springdemo.model.req.UserReq;
import issac.study.springdemo.model.vo.UserVo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author humy6
 */
@RestController
@RequestMapping("/feign")
public class FeignController {

    @GetMapping("/{id}")
    public UserVo getById(@PathVariable("id") Integer id) throws InterruptedException {
        Thread.sleep(1000);
               return new UserVo(id,"name"+Math.random());
    }

    @RequestMapping("/list")
    public List<UserVo> list(@RequestBody UserReq req, PageReq pageReq){
        System.out.println(JSON.toJSON(req));
        System.out.println(JSON.toJSON(pageReq));
        List<UserVo> userVos=new ArrayList<>();
        userVos.add(new UserVo(1,"name"+Math.random()));
        userVos.add(new UserVo(2,"name"+Math.random()));
        return userVos;
    }
}
