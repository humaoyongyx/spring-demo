package issac.study.springdemo.service.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author humy6
 * @Date: 2019/7/18 16:33
 */
@Builder
@Data
public class SimpleVo {

    private Integer id;
    private Integer id2;
    private Integer id3;
    private String name;

    @Tolerate
    public SimpleVo(){}
}
