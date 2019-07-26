package issac.study.springdemo.core.config.message;

import issac.study.springdemo.core.context.ContextHolder;
import issac.study.springdemo.core.context.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
        return    messageSource.getMessage(code,null, ContextHolder.getUser().orElse(User.builder().build()).getLocale());
    }
}
