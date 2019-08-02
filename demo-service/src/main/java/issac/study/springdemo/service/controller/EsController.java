package issac.study.springdemo.service.controller;

import issac.study.springdemo.service.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 * @Date: 2019/7/25 13:08
 */

@RestController
@RequestMapping("/es")
public class EsController {

    @Autowired
    EsService esService;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @RequestMapping("/find")
    public Object find(String name){
       return esService.test1(name);
    }

    @RequestMapping("/save")
    public Object save(String key,String name){
        return esService.save(key,name);
    }

    @RequestMapping("/index")
    public boolean index(String name){
        IndexQuery indexQuery=new IndexQuery();
        return  esService.createIndex(name);
    }

    @RequestMapping("/search")
    public Object search(String name, @PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return  esService.getIdeaListBySrt(name,pageable);
    }
    @RequestMapping("/save2")
    public void save2( ){
         esService.save();
    }
    @RequestMapping("/del")
    public void del( ){
        esService.delete();
    }


}
