package issac.study.springdemo.core.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author humy6
 * @Date: 2019/7/16 10:31
 */
@Configuration
@ConfigurationProperties("multi.ds")
public class MultiDataSourceProp {

    private String primaryKey;
    private List<DataSourceProp> dataSources;

    public List<DataSourceProp> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceProp> dataSources) {
        this.dataSources = dataSources;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public static class DataSourceProp{

        private String dsKey;
        private String username;
        private String password;
        private String driverClassName;
        private String url;

        public String getDsKey() {
            return dsKey;
        }

        public void setDsKey(String dsKey) {
            this.dsKey = dsKey;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
