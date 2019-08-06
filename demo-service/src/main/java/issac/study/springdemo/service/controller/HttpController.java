package issac.study.springdemo.service.controller;

import com.alibaba.fastjson.JSON;
import issac.study.springdemo.core.config.http.HttpClientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/8/6 15:10
 */
@RestController
@RequestMapping("http")
public class HttpController {

    @Autowired
    HttpClientHelper httpClientHelper;


    @RequestMapping("/1")
    public String test1(){
        return httpClientHelper.get("https://localhost");
    }

    @RequestMapping("/2")
    public String test2(){
        Map<String, String> headers = new HashMap<>();
        headers.put("mroToken", "test");
        Map<String, Object> params = new HashMap<>();
        params.put("size", 1);
        Map map = new HashMap();
        map.put("equipmentTypeId", 1);
        String result = httpClientHelper.postWithJsonBody("http://localhost:8080/mro-base/equipment/page", params, headers, JSON.toJSONString(map));
        return result;
    }
}
