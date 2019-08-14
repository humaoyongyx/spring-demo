package issac.study.springdemo.core.config.es;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author humy6
 * @Date: 2019/7/31 10:09
 */
@Configuration
@ConditionalOnClass(ElasticsearchConverter.class)
@EnableElasticsearchRepositories
public class EsConfig {
/*
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client, ElasticsearchConverter converter) {
        try {
            return new ElasticsearchTemplate(client, converter,new MyResultMapper(converter.getMappingContext()));
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }*/

  @Bean
    MyResultMapper myResultMapper( ElasticsearchConverter converter){
        return new MyResultMapper(converter.getMappingContext());
    }

}
