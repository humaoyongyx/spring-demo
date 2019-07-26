package issac.study.springdemo.service.mongo;

import issac.study.springdemo.service.model.Tb1;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author humy6
 * @Date: 2019/7/17 16:21
 */
public interface TestMongoDao extends MongoRepository<Tb1,Integer> {
}
