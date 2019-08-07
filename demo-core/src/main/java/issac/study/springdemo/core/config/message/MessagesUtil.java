package issac.study.springdemo.core.config.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author humy6
 * @Date: 2019/7/22 8:45
 */
@Component
public class MessagesUtil {
    @Autowired
    MessageSource messageSource;

    public String get(String code){
        return    messageSource.getMessage(code,null, LocaleContextHolder.getLocale());
    }
}
