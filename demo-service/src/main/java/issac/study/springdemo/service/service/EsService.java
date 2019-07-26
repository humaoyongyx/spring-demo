package issac.study.springdemo.service.service;

import issac.study.springdemo.service.es.EsDao;
import issac.study.springdemo.service.model.EsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

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

  public Object test1(String name){
      return  esDao.findByName(name);
  }


    public Object save(String key,String name){
        EsBean esBean = new EsBean();
        esBean.setName(name);
        esBean.setKey(key);
        return  esDao.save(esBean);
    }
  public boolean createIndex(){
      return elasticsearchTemplate.createIndex(EsBean.class);
  }

}
