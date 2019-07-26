package issac.study.springdemo.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author humy6
 * @Date: 2019/7/16 11:30
 */

@Configuration
public class TransactionConfig {


    /**
     * mybatis事务管理器
     * @param dataSource
     * @return
     */
    @Bean
    PlatformTransactionManager mybatisTransactionManager(DataSource dataSource){
        return    new DataSourceTransactionManager(dataSource)  ;
    }


    /**
     * 指定jpa的事务，@Primary 多个的时候，选择此事务
     * @param entityManagerFactory
     * @return
     */
    @Bean
    @Primary
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

}
