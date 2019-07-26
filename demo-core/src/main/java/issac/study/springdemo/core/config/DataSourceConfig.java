package issac.study.springdemo.core.config;

import issac.study.springdemo.core.config.cache.DataSourceMapCache;
import issac.study.springdemo.core.config.prop.MultiDataSourceProp;
import issac.study.springdemo.core.context.ContextHolder;
import issac.study.springdemo.core.context.User;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/7/16 10:56
 */

@Configuration
public class DataSourceConfig {

    /**
     * 从配置文件读取数据源
     * @param multiDataSourceProp
     * @return
     */
    @Bean("multiDataSourceMap")
    public Map<Object, Object> multiDataSourceMap(MultiDataSourceProp multiDataSourceProp){

        Map<Object,Object> dataSourceMap= DataSourceMapCache.getDataSourceMap();
        multiDataSourceProp.getDataSources().stream().forEach(
                it->{
                    DataSource ds = DataSourceBuilder.create().url(it.getUrl())
                            .driverClassName(it.getDriverClassName())
                            .username(it.getUsername())
                            .password(it.getPassword())
                            .build();
                    dataSourceMap.put(it.getDsKey(),ds);
                }
        );
        return dataSourceMap;
    }

    /**
     * 动态数据源
     * @return
     */
    @Bean
    @DependsOn("multiDataSourceMap")
    DataSource dataSource( MultiDataSourceProp multiDataSourceProp){
        AbstractRoutingDataSource routingDataSource=  new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                //解决动态添加数据源的bug
                this.setTargetDataSources(DataSourceMapCache.getDataSourceMap());
                this.afterPropertiesSet();
                return ContextHolder.getUser().orElse(User.builder().dsKey(multiDataSourceProp.getPrimaryKey()).build()).getDsKey();
            }
        };

        routingDataSource.setTargetDataSources(DataSourceMapCache.getDataSourceMap());
        routingDataSource.setDefaultTargetDataSource(DataSourceMapCache.getDataSourceMap().get(multiDataSourceProp.getPrimaryKey()));
      return   routingDataSource;
    }


}
