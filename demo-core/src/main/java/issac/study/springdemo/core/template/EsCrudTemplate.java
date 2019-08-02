package issac.study.springdemo.core.template;

import issac.study.springdemo.core.config.es.MyResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

/**
 * @author humy6
 * @Date: 2019/7/31 8:33
 */

@Component
public class EsCrudTemplate {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    MyResultMapper myResultMapper;

    public <E> Page pageHighlight(SearchQuery searchQuery,Class<E> eClass){
        return elasticsearchTemplate.queryForPage(searchQuery,eClass,myResultMapper);
    }

    /**
     *  如果接口里面有@document的bean，和xxxQuery，默认会从xxxQuery中找index和type，然后才从bean中的注解中寻找
     * @param bean
     */
    public void save(Object bean){
        IndexQuery indexQuery=new IndexQuery();
        indexQuery.setObject(bean);
        indexQuery.setIndexName("multi_test");
        elasticsearchTemplate.index(indexQuery);
    }

    public void delete(String id){
        DeleteQuery deleteQuery=new DeleteQuery();
        deleteQuery.setIndex("multi_test");
        elasticsearchTemplate.delete("multi_test","_doc",id);
    }



}
