package issac.study.springdemo.core.config;

import issac.study.springdemo.core.filter.JsonRequestFilter;
import issac.study.springdemo.core.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author humy6
 * @Date: 2019/8/2 8:32
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean localeFilterRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new JsonRequestFilter());
        filterRegistrationBean.setName("JsonRequestFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
