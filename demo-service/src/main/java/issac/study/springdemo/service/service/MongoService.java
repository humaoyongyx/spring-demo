package issac.study.springdemo.service.service;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import issac.study.springdemo.service.model.Tb1;
import issac.study.springdemo.service.mongo.TestMongoDao;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author humy6
 * @Date: 2019/7/17 16:23
 */
@Service
public class MongoService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TestMongoDao testMongoDao;


    public Object get(){
        MongoCollection<Document> t1 = mongoTemplate.getDb().getCollection("tb1");
        FindIterable<Document> documents = t1.find();
        List<Document> list=new ArrayList();
        documents.projection(Document.parse("{_id:0}")).forEach((Block<Document>) document -> list.add(document));
        return list;
    }

    public Object save(){
        MongoCollection<Document> t1 = mongoTemplate.getDb().getCollection("tb1");
        t1.insertOne(Document.parse("{id:1,name:'xxx'}"));
        return "success";
    }

    public Object save2(){
        testMongoDao.save(new Tb1.Builder().id(111).name("user111").build());
        return "success";
    }
}
