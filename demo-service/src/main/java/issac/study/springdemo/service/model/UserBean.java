package issac.study.springdemo.service.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author humy6
 * @Date: 2019/7/16 12:44
 */
@Entity
@Table(name = "user")
public class UserBean extends BaseBean{

    private String name;
    public UserBean(){}

    private UserBean(Builder builder) {
        setId(builder.id);
        setCreateBy(builder.createBy);
        setUpdateBy(builder.updateBy);
        setTimeCreated(builder.timeCreated);
        setTimeUpdated(builder.timeUpdated);
        setName(builder.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static final class Builder {
        private Integer id;
        private Integer createBy;
        private Integer updateBy;
        private Date timeCreated;
        private Date timeUpdated;
        private String name;

        public Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder createBy(Integer val) {
            createBy = val;
            return this;
        }

        public Builder updateBy(Integer val) {
            updateBy = val;
            return this;
        }

        public Builder timeCreated(Date val) {
            timeCreated = val;
            return this;
        }

        public Builder timeUpdated(Date val) {
            timeUpdated = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public UserBean build() {
            return new UserBean(this);
        }
    }
}
