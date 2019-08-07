package issac.study.springdemo.core.validator;

import issac.study.springdemo.core.context.SpringContextHolder;
import issac.study.springdemo.core.validator.annotation.DateValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;

/**
 * @author humy6
 * @Date: 2019/8/7 9:17
 */
public class DateValidatorConstraint implements ConstraintValidator<DateValidator,String> {

    private DateTimeFormatter dateTimeFormatter;
    private String formatter;
    private MessageSource messageSource;

    @Override
    public void initialize(DateValidator constraintAnnotation) {
        this.formatter=constraintAnnotation.formatter();
        this.dateTimeFormatter=DateTimeFormatter.ofPattern( this.formatter);
        this.messageSource = SpringContextHolder.getBean("messageSource", MessageSource.class);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       if (StringUtils.isNotBlank(value)){
           try {
               dateTimeFormatter.parse(value);
           }catch (Exception e){
               String defaultMessage = context.getDefaultConstraintMessageTemplate();
               String message = messageSource.getMessage("core.validator.date.message", new Object[]{this.formatter}, LocaleContextHolder.getLocale());
               context.disableDefaultConstraintViolation();
               context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
               return false;
           }

       }
        return true;
    }
}
