package issac.study.springdemo.service.service;

import issac.study.springdemo.core.template.EsCrudTemplate;
import issac.study.springdemo.service.es.EsDao;
import issac.study.springdemo.service.model.EsBean;
import org.apache.commons.lang3.RandomUtils;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * @author humy6
 * @Date: 2019/7/25 13:10
 */
@Service
public class EsService {

  @Autowired
    EsDao esDao;

  @Autowired
  ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
    EsCrudTemplate esCrudTemplate;

  public Object test1(String name){
      return  esDao.findByName(name);
  }


    public Object save(String key,String name){
        EsBean esBean = new EsBean();
        esBean.setId(RandomUtils.nextInt(1,10000));
        esBean.setName(name);
        return  esDao.save(esBean);
    }
  public boolean createIndex(String name){
      return elasticsearchTemplate.createIndex(name);
  }

  public void test(){
  }


    public Page getIdeaListBySrt(String name, Pageable pageable) {
        String preTag = "<font color='red'>";
        String postTag = "</font>";
        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(matchQuery("name.pinyin", name)).
                withHighlightFields(new HighlightBuilder.Field("name.pinyin").preTags(preTag).postTags(postTag)).build();
        searchQuery.setPageable(pageable);


        Page page = esCrudTemplate.pageHighlight(searchQuery,EsBean.class);
        return page;
    }

    public void save(){
       EsBean esBean=new EsBean();
       esBean.setId(10);
       esBean.setName("测试multi");
       esCrudTemplate.save(esBean);
    }
    public void delete(){
        esCrudTemplate.delete(10+"");
    }

}
