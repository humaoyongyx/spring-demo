package issac.study.springdemo.service;

import issac.study.springdemo.service.interceptor.HolderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局Mvc
 */
@Configuration
public class GlobalMvcConfig implements WebMvcConfigurer {

    @Autowired
    private HolderInterceptor holderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(holderInterceptor);

    }

}
