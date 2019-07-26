package issac.study.springdemo.core.config;

import issac.study.springdemo.core.config.cache.DataSourceMapCache;
import issac.study.springdemo.core.config.prop.MultiDataSourceProp;
import issac.study.springdemo.core.context.ContextHolder;
import issac.study.springdemo.core.context.User;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/7/16 11:23
 */

@Configuration
@EnableJpaRepositories(basePackages = "issac.study.springdemo.**.repository")
public class MultiJpaConfig {


    @Bean
    @DependsOn("multiDataSourceMap")
    MultiTenantConnectionProvider multiTenantConnectionProvider( MultiDataSourceProp multiDataSourceProp){
        return  new AbstractDataSourceBasedMultiTenantConnectionProviderImpl() {
            @Override
            protected DataSource selectAnyDataSource() {
                return (DataSource) DataSourceMapCache.getDataSourceMap().get(multiDataSourceProp.getPrimaryKey());
            }

            @Override
            protected DataSource selectDataSource(String s) {
                return (DataSource) DataSourceMapCache.getDataSourceMap().get(s);
            }
        };
    }

    @Bean
    CurrentTenantIdentifierResolver currentTenantIdentifierResolver(MultiDataSourceProp multiDataSourceProp){

        return new CurrentTenantIdentifierResolver() {
            @Override
            public String resolveCurrentTenantIdentifier() {
                return ContextHolder.getUser().orElse(User.builder().dsKey(multiDataSourceProp.getPrimaryKey()).build()).getDsKey();
            }

            @Override
            public boolean validateExistingCurrentSessions() {
                return false;
            }
        };
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                       CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        //默认为none
        hibernateProps.put(Environment.HBM2DDL_AUTO, "update");
        hibernateProps.put(Environment.SHOW_SQL, true);
        hibernateProps.put(Environment.FORMAT_SQL, true);

        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setPackagesToScan("issac.study.**.model");
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        result.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        result.setJpaPropertyMap(hibernateProps);
        return result;
    }

}
