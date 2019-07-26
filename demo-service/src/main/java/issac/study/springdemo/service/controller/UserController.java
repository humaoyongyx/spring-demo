package issac.study.springdemo.service.controller;

import issac.study.springdemo.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 * @Date: 2019/7/16 13:13
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/jpa/save")
    public void save(){
        userService.saveJpa();
    }

    @RequestMapping("/jpa/update")
    public void update(@RequestParam Integer id){
        userService.updateJpa(id);
    }

    @RequestMapping("/jpa/saveErr")
    public void saveErr(){
        userService.saveJpaErr();
    }

    @RequestMapping("/mybatis/save")
    public void saveMybatis(){
        userService.saveMybatis();
    }

    @RequestMapping("/mybatis/saveErr")
    public void saveMybatisErr(){
        userService.saveMybatisErr();
    }


    @RequestMapping("/get")
    public Object get(Integer id){
        return userService.get(id);
    }
}
