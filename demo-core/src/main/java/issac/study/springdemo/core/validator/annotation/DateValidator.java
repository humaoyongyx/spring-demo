package issac.study.springdemo.core.validator.annotation;

import issac.study.springdemo.core.validator.DateValidatorConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author humy6
 * @Date: 2019/8/7 9:13
 */
@Constraint(validatedBy = DateValidatorConstraint.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateValidator {

    String message() default "{common.validator.date.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String formatter() default "yyyy-MM-dd HH:mm:ss";
}
