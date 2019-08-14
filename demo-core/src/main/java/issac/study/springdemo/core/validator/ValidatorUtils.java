package issac.study.springdemo.core.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

/**
 * @author humy6
 * @Date: 2019/8/7 11:04
 */
@Component
public class ValidatorUtils {

    @Autowired
    MessageSource messageSource;

    public String formatMessage(BindException bindException){
        FieldError fieldError = bindException.getBindingResult().getFieldError();
        String message = messageSource.getMessage("core.validator.format.message", new Object[]{fieldError.getField(), fieldError.getRejectedValue(), MessageUtils.get(fieldError.getDefaultMessage())}, LocaleContextHolder.getLocale());
        return message;
    }



}
