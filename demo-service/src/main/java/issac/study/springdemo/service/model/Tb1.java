package issac.study.springdemo.service.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author humy6
 * @Date: 2019/7/23 15:30
 */
@Document("tb1")
public class Tb1 {

    private Integer id;
    private String name;

    private Tb1(Builder builder) {
        setId(builder.id);
        setName(builder.name);
    }

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

    public static final class Builder {
        private Integer id;
        private String name;

        public Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Tb1 build() {
            return new Tb1(this);
        }
    }
}
