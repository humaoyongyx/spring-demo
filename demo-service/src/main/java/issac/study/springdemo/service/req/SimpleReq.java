package issac.study.springdemo.service.req;

import issac.study.springdemo.core.validator.annotation.DateValidator;

import javax.validation.constraints.NotNull;

/**
 * @author humy6
 * @Date: 2019/8/7 9:24
 */
public class SimpleReq {

    @NotNull
    @DateValidator
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
