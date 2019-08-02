package issac.study.springdemo.service.controller;

import com.alibaba.fastjson.JSON;
import issac.study.springdemo.core.template.MongoCrudTemplate;
import issac.study.springdemo.service.model.UserBean;
import issac.study.springdemo.service.service.MongoService;
import issac.study.springdemo.service.vo.SimpleVo;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author humy6
 * @Date: 2019/7/17 16:32
 */

@RestController
@RequestMapping("mongo")
public class MongoDbController {

    @Autowired
    MongoService mongoService;

    @Autowired
    MongoCrudTemplate mongoCrudTemplate;

    @RequestMapping("/get")
    public Object test1(){

        return mongoService.get();
    }

    @RequestMapping("/save")
    public Object save(){
        return mongoService.save();
    }

    @RequestMapping("/del")
    public void del(Integer id){
         mongoCrudTemplate.delete("tb1",id);
    }

    @RequestMapping("/save2")
    public Object save2(){
        return mongoService.save2();
    }

    @RequestMapping("/save3")
    public Object save3(@RequestParam Integer id,@RequestParam String name){
        mongoCrudTemplate.saveOrUpdate("tb1",new UserBean.Builder().id(id).name(name).build());
        return "ss";
    }

    @RequestMapping("/save4")
    public Object save3(Integer id2,Integer id3,@RequestParam String name){
        mongoCrudTemplate.saveOrUpdate("tb1", SimpleVo.builder().id2(id2).id3(id3).name(name).build(),new String[]{"id2","id3"});
        return "ss";
    }

    @RequestMapping("/find1")
    public Object find1(@RequestParam List<Integer> ids){
        return mongoCrudTemplate.findByIds("tb1",ids);
    }

    @RequestMapping("/saveJson")
    public void saveJson(){
        Map<String,Object> map=new HashMap<>();
        map.put("id", RandomUtils.nextInt(0,100));
        map.put("name","测试名称"+RandomUtils.nextInt(0,100));
        map.put("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        mongoCrudTemplate.saveOrUpdate("tb1", JSON.toJSONString(map));
    }
    @RequestMapping("/getJson")
    public Object getJson(){
        return mongoCrudTemplate.findByIds("tb1", Arrays.asList(10));
    }

    @RequestMapping("/test")
    public Object test(@PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, String test){
        return mongoCrudTemplate.buildQuery().orLike("name","测").orLike("name","小").andGe("date","2019-07-23 15:06:06").andLe("date","2019-07-23 15:06:06").query("tb1",pageable);
    }
    @RequestMapping("/total")
    public Object total(@PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, String test){
        return mongoCrudTemplate.buildQuery().orLike("name","小").orGe("date","2019-07-23 10:00:00").total("tb1");
    }
}
