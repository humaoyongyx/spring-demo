package issac.study.springdemo.service.es;


import issac.study.springdemo.service.model.EsBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author humy6
 * @Date: 2019/7/25 13:04
 */
public interface EsDao extends ElasticsearchRepository<EsBean,Integer> {

    EsBean findByName(String name);
}
