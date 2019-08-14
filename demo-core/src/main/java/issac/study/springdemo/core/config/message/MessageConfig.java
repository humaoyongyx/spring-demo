package issac.study.springdemo.core.config.message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.util.Locale;

/**
 * @author humy6
 * @Date: 2019/7/22 8:28
 */

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //应用jar包项目里面，如果有相同的文件，会覆盖，这个覆盖是指只要找到第一个文件就不会在去遍历其他目录，这样就导致了相同文件里面的属性也必须全部覆写，否则会找不到属性
        messageSource.setBasenames("i18n/core/messages","i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public Validator validator(MessageSource messageSource){
        LocalValidatorFactoryBean localValidatorFactoryBean=new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        localValidatorFactoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        return localValidatorFactoryBean;
    }

    @Bean
    LocaleResolver localeResolver(){
        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return new LocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String lang = request.getParameter("i18n");
                Locale locale;
                if (StringUtils.isNotBlank(lang)){
                    String[] langCountry = lang.split("_");
                    locale=new Locale(langCountry[0],langCountry[1]);
                }else {
                    locale= acceptHeaderLocaleResolver.resolveLocale(request);
                }
                return locale;
            }

            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

            }
        };
    }
}