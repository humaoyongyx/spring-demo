package issac.study.springdemo.core.config.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/7/16 15:01
 */
public class DataSourceMapCache {

    private static final Map<Object, Object> dataSourceMap=new HashMap<>();

     public  static Map<Object, Object>  getDataSourceMap(){
         return dataSourceMap;
     }

     public static void  add(String key,DataSource dataSource){
            if (StringUtils.isNotBlank(key)){
                if (dataSourceMap.get(key)==null){
                    dataSourceMap.put(key,dataSource);
                }
            }
     }

     public  static void addTest(){
         add("ds3",DataSourceBuilder.create().driverClassName("org.h2.Driver").
                 url("jdbc:h2:mem:h2test3;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE").
                 username("sa").
                 build());
     }

}
