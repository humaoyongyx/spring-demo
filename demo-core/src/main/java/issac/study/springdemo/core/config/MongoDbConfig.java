package issac.study.springdemo.core.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import issac.study.springdemo.core.config.cache.MongoDbMapCache;
import issac.study.springdemo.core.config.prop.MultiMongoDbProp;
import issac.study.springdemo.core.context.ContextHolder;
import issac.study.springdemo.core.template.MongoCrudTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @author humy6
 * @Date: 2019/7/18 8:37
 */

/**
 * 懒加载，引入此包必须显示依赖mongodb jar包，如果enable不为true则默认走springboot自己的automongo配置
 */
@Configuration
@ConditionalOnClass(MongoDbFactory.class)
@ConditionalOnProperty(prefix = "multi.mongodb",name = "enable",havingValue = "true")
@EnableMongoRepositories(basePackages = "issac.study.springdemo.**.mongo")
public class MongoDbConfig {

    @Autowired
    MultiMongoDbProp multiMongoDbProp;
    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        multiMongoDbProp.getMongoDbProps().stream().forEach(
                it -> {
                    MongoClientURI mongoClientURI = new MongoClientURI(it.getUri());
                    MongoClient mongoClient = new MongoClient(mongoClientURI);
                    MongoDbMapCache.get().put(it.getDsKey(), new MongoDbMapCache.MongoBean.Builder().mongoClient(mongoClient).mongoClientURI(mongoClientURI).build());
                }
        );

    }

    @Bean
    public MongoDbFactory mongoDbFactory() {
        String primaryKey = multiMongoDbProp.getPrimaryKey();
        MongoDbMapCache.MongoBean mongoBean = MongoDbMapCache.get().get(primaryKey);
        return new MultiMongoDbFactory(mongoBean.getMongoClient(),mongoBean.getMongoClientURI().getDatabase());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter converter) {
        return new MongoTemplate(mongoDbFactory, converter);
    }


    @Bean
    public MongoCrudTemplate mongoCrudTemplate(MongoTemplate mongoTemplate){
         return new MongoCrudTemplate(mongoTemplate);
    }


    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }


    @Bean
    public MongoMappingContext mongoMappingContext(MongoCustomConversions conversions) throws ClassNotFoundException {
        MongoMappingContext context = new MongoMappingContext();
        context.setInitialEntitySet((new EntityScanner(this.applicationContext)).scan(new Class[]{Document.class, Persistent.class}));
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        return context;
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Collections.emptyList());
    }

    private static class MultiMongoDbFactory extends SimpleMongoDbFactory {


        public MultiMongoDbFactory(MongoClient mongoClient, String databaseName) {
            super(mongoClient, databaseName);
        }

        @Override
        public MongoDatabase getDb() throws DataAccessException {
            if (ContextHolder.getUser().isPresent()) {
                String dsKey = ContextHolder.getUser().get().getDsKey();
                if (StringUtils.isNotBlank(dsKey)){
                    MongoDbMapCache.MongoBean mongoBean = MongoDbMapCache.get().get(dsKey);
                    if (mongoBean != null) {
                        return mongoBean.getMongoClient().getDatabase(mongoBean.getMongoClientURI().getDatabase());
                    }else {
                        throw new RuntimeException("mongoDb不存在");
                    }
                }
            }
            return super.getDb();

        }
    }

}
