package issac.study.springdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humy6
 * @Date: 2019/7/16 14:00
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionAdvise {

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception ex){
            log.error("global ex:",ex);
            return "系统异常："+ex.getMessage();
    }
}


