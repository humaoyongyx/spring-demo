package issac.study.springdemo.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author humy6
 * @Date: 2019/7/25 12:55
 */
@Document(indexName = "test",createIndex = false,type = "_doc")
public class EsBean {

    @Id
    private Integer id;
    private String name;
    /**
     * 使用高亮必须指定这个字段，然后自定义一个pojo Highlight 对象，程序会自动将名称中的.和_替换为字母大写
     */
    private Highlight highlight;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Highlight getHighlight() {
        return highlight;
    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }
}
