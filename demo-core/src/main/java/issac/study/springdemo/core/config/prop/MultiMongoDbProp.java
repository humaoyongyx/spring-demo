package issac.study.springdemo.core.config.prop;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author humy6
 * @Date: 2019/7/16 10:31
 */
@Configuration
@ConfigurationProperties("multi.mongodb")
public class MultiMongoDbProp {

    private Boolean enable;
    private String primaryKey;
    private List<MongoDbProp> mongoDbProps;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<MongoDbProp> getMongoDbProps() {
        return mongoDbProps;
    }

    public void setMongoDbProps(List<MongoDbProp> mongoDbProps) {
        this.mongoDbProps = mongoDbProps;
    }

    public static class MongoDbProp extends MongoProperties {

        private String dsKey;

        public String getDsKey() {
            return dsKey;
        }

        public void setDsKey(String dsKey) {
            this.dsKey = dsKey;
        }


    }
}
