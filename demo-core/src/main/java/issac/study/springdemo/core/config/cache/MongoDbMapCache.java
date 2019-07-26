package issac.study.springdemo.core.config.cache;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.HashMap;
import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/7/18 9:09
 */
public class MongoDbMapCache {

    private static final Map<String, MongoBean> mongoDbClientMap =new HashMap<>();

    public static Map<String, MongoBean> get(){
        return mongoDbClientMap;
    }

   public static void addTest(){
       MongoClientURI mongoClientURI = new MongoClientURI("mongodb://localhost/test3");
       MongoClient mongoClient = new MongoClient(mongoClientURI);
       mongoDbClientMap.put("ds3",new MongoBean.Builder().mongoClientURI(mongoClientURI).mongoClient(mongoClient).build());
   }

    public static class MongoBean{

        private MongoClientURI mongoClientURI;
        private MongoClient mongoClient;

        private MongoBean(Builder builder) {
            setMongoClientURI(builder.mongoClientURI);
            setMongoClient(builder.mongoClient);
        }

        public MongoClientURI getMongoClientURI() {
            return mongoClientURI;
        }

        public void setMongoClientURI(MongoClientURI mongoClientURI) {
            this.mongoClientURI = mongoClientURI;
        }

        public MongoClient getMongoClient() {
            return mongoClient;
        }

        public void setMongoClient(MongoClient mongoClient) {
            this.mongoClient = mongoClient;
        }


        public static final class Builder {
            private MongoClientURI mongoClientURI;
            private MongoClient mongoClient;

            public Builder() {
            }

            public Builder mongoClientURI(MongoClientURI val) {
                mongoClientURI = val;
                return this;
            }

            public Builder mongoClient(MongoClient val) {
                mongoClient = val;
                return this;
            }

            public MongoBean build() {
                return new MongoBean(this);
            }
        }
    }

}
