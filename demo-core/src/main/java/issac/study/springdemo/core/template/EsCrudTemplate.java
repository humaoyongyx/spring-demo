package issac.study.springdemo.core.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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

    public <E> Page pageHighlight(SearchQuery searchQuery,Class<E> eClass){
        return elasticsearchTemplate.queryForPage(searchQuery,eClass);
    }



}
