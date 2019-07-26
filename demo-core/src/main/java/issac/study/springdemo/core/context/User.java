package issac.study.springdemo.core.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * @author humy6
 * @Date: 2019/7/16 10:21
 */
@Setter
@Getter
@Builder
public class User {

    private int id;
    private String dsKey;
    private Locale locale=Locale.CHINESE;

}
