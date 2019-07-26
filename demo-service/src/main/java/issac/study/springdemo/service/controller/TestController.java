package issac.study.springdemo.service.controller;

import issac.study.springdemo.core.config.cache.DataSourceMapCache;
import issac.study.springdemo.core.config.cache.MongoDbMapCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 * @Date: 2019/7/16 15:09
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/addDs")
    public Object testAddDs(){
        DataSourceMapCache.addTest();
        MongoDbMapCache.addTest();
        return DataSourceMapCache.getDataSourceMap().keySet();
    }

}
