package issac.study.springdemo.core.template;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.ReplaceOptions;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 自定义mongo数据库动态查询类
 *
 * @author humy6
 */

public class MongoCrudTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoCrudTemplate.class);

    /**
     * 常量定义
     */
    private static final String ID = "id";
    private static final String OBJECT_ID = "_id";
    private static final String EQ = "$eq";
    private static final String NE = "$ne";
    private static final String OR = "$or";
    private static final String IN = "$in";
    private static final String NIN = "$nin";
    private static final String GT = "$gt";
    private static final String GTE = "$gte";
    private static final String LT = "$lt";
    private static final String LTE = "$lte";
    private static final String REGEX = "$regex";
    private static final String LIKE_PREFIX = "^";
    private static final String NOT = "$not";
    private static final String DB_SIGN = "%";


    private MongoTemplate mongoTemplate;

    /**
     * 构造
     *
     * @param mongoTemplate
     */
    public MongoCrudTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 更新或保存
     *
     * @param tableName CollectionName集合名称
     * @param pojoBean  普通的javaBean
     * @param uid       不指定的话，更新默认找的是id；可以指定多个字段作为唯一条件
     */
    public void saveOrUpdate(String tableName, Object pojoBean, String... uid) {
        Objects.requireNonNull(pojoBean);
        String pojoJson = JSON.toJSONString(pojoBean);
        Document document = Document.parse(pojoJson);
        save(tableName, document, uid);

    }

    /**
     * 更新或保存
     *
     * @param tableName CollectionName集合名称
     * @param json      json格式
     * @param uid       不指定的话，更新默认找的是id；可以指定多个字段作为唯一条件
     */
    public void saveOrUpdate(String tableName, String json, String... uid) {
        Objects.requireNonNull(json);
        Document document = Document.parse(json);
        save(tableName, document, uid);

    }

    /**
     * 删除
     *
     * @param tableName
     * @param id
     */
    public void delete(String tableName, Integer id) {
        MongoCollection<Document> collection = mongoTemplate.getDb().getCollection(tableName);
        collection.deleteOne(new BasicDBObject(ID, id));
    }

    private void save(String tableName, Document document, String... uid) {
        BasicDBObject basicDBObject = null;
        if (uid != null && uid.length > 0) {
            basicDBObject = new BasicDBObject();
            for (String id : uid) {
                Object o = document.get(id);
                if (o != null) {
                    basicDBObject.put(id, o);
                } else {
                    return;
                }
            }
        } else {
            Object idVal = document.get(ID);
            if (idVal != null) {
                basicDBObject = new BasicDBObject();
                basicDBObject.put(ID, idVal);
            }
        }
        MongoCollection<Document> collection = mongoTemplate.getDb().getCollection(tableName);
        if (basicDBObject == null) {
            collection.insertOne(document);
        } else {
            ReplaceOptions replaceOptions = new ReplaceOptions();
            replaceOptions.upsert(true);
            collection.replaceOne(basicDBObject, document, replaceOptions);
        }
    }

    public Optional<Map<String,Object>> findById(String tableName, Integer id) {
        return findById(tableName, id, null);
    }

    /**
     * 根据id获取值
     *
     * @param tableName
     * @param id
     * @param idName
     * @return
     */
    public Optional<Map<String,Object>> findById(String tableName, Integer id, String idName) {
        String fieldName;
        if (StringUtils.isBlank(idName)) {
            fieldName = ID;
        } else {
            fieldName = idName;
        }
        List<Map<String,Object>> query = buildQuery().andEq(fieldName, id).query(tableName);
        if (!query.isEmpty()) {
            return Optional.of(query.get(0));
        }
        return Optional.empty();
    }


    public List<Map<String,Object>> findByIds(String tableName, List<Integer> ids) {
        return findByIds(tableName, ids, null);
    }

    /**
     * @param tableName CollectionName集合名称
     * @param ids       id列表
     * @param idName    为null的话，默认为id，可以指定id的名称
     * @return
     */
    public List<Map<String,Object>> findByIds(String tableName, List<Integer> ids, String idName) {

        if (ids == null || ids.isEmpty()) {
            new ArrayList<>();
        }
        BasicDBList items = new BasicDBList();
        String fieldName;
        if (StringUtils.isNotBlank(idName)) {
            fieldName = idName;
        } else {
            fieldName = ID;
        }
        for (Integer id : ids) {
            items.add(id);
        }
        return buildQuery().andIn(fieldName, ids).query(tableName);
    }


    /**
     * 构造query查询类
     *
     * @return
     */
    public Query buildQuery() {
        return new Query(mongoTemplate);
    }

    public static class Query {

        private MongoTemplate mongoTemplate;

        private BasicDBObject andObject = new BasicDBObject();

        private BasicDBList orList = new BasicDBList();

        public Query(MongoTemplate mongoTemplate) {
            this.mongoTemplate = mongoTemplate;
        }

        /**
         * 参数检查
         *
         * @param operator
         * @param name
         * @param value
         * @return
         */
        private Query check(Operator operator, String name, Object value) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(operator);

            if (operator == Operator.IN) {
                if (!(value instanceof Collection)) {
                    return this;
                }
            }
            if (operator != Operator.IS_NULL) {
                if (value == null) {
                    return this;
                }
                if (value instanceof String) {
                    if (StringUtils.isBlank((String) value)) {
                        return this;
                    }
                }
            }
            return null;
        }


        /**
         *   根据操作符，默认处理一些值，如like 将%转换为mongdb认识的值
         * @param operator
         * @param name
         * @param value
         * @return
         */
        private Object resetValue(Operator operator, String name, Object value) {

            if (operator == Operator.LIKE || operator == Operator.NOT_LIKE) {
                String temp = (String) value;
                if (temp.contains(DB_SIGN)) {
                    String temp2=temp;
                    temp = temp.replace(DB_SIGN, "");
                    if (!temp2.startsWith(DB_SIGN)) {
                        temp = LIKE_PREFIX + temp;
                    }else if (!temp2.endsWith(DB_SIGN)){
                        temp =  temp+LIKE_PREFIX ;
                    }
                } else {
                    temp = LIKE_PREFIX + temp;
                }
                value = Pattern.compile(temp, Pattern.CASE_INSENSITIVE);
            }
            return value;
        }

        /**
         * 处理   value>=&&value<= 这种两种操作符的值，否则与操作的时候，由于是map，会自动覆盖
         * @param name
         * @param operator
         * @param value
         * @param isOr
         * @return
         */
        private BasicDBObject handleBinaryOperator(String name,String operator,Object value,boolean isOr){
            BasicDBObject result= new BasicDBObject(operator, value);
            if (isOr){
                if (GT.equals(operator)||GTE.equals(operator)||LT.equals(operator)||LTE.equals(operator)){
                    Iterator<Object> iterator = this.orList.iterator();
                    while (iterator.hasNext()){
                        BasicDBObject item = (BasicDBObject) iterator.next();
                        BasicDBObject existField = (BasicDBObject) item.get(name);
                        if (existField!=null){
                            result = appendBinaryExistField(operator, value, result, existField);
                            iterator.remove();
                            break;
                        }
                    }
                }
            }else {
                BasicDBObject existField = (BasicDBObject) this.andObject.get(name);
                if (existField!=null){
                    result = appendBinaryExistField(operator, value, result, existField);
                }
            }
            return result;
        }

        private BasicDBObject appendBinaryExistField(String operator, Object value, BasicDBObject result, BasicDBObject existField) {
            if (GT.equals(operator) || GTE.equals(operator)) {
                if (existField.containsField(LT) || existField.containsField(LTE)) {
                    if (!existField.containsField(operator)) {
                        result = existField.append(operator, value);
                    }
                }
            } else if (LT.equals(operator) || LTE.equals(operator)) {
                if (existField.containsField(GT) || existField.containsField(GTE)) {
                    if (!existField.containsField(operator)) {
                        result = existField.append(operator, value);
                    }
                }
            }
            return result;
        }

        /**
         * 查询方法 and做连接 类似sql示例( where q1>0  and( q2=3 or q2 =4))
         *
         * @param operator 操作因子
         * @param name     字段名称
         * @param value    字段值
         * @param isOr     是否是or查询
         * @return
         */
        public Query and(Operator operator, String name, Object value, boolean isOr) {
            if (check(operator, name, value) != null) {
                return this;
            }
            value = resetValue(operator, name, value);
            Object temp=null;
            switch (operator) {
                case EQ:
                    temp = new BasicDBObject(EQ, value);
                    break;
                case NE:
                    temp = new BasicDBObject(NE, value);
                    break;
                case LIKE:
                    temp = new BasicDBObject(REGEX, value);
                    break;
                case NOT_LIKE:
                    temp = new BasicDBObject(NOT, value);
                    break;
                case LT:
                    temp = handleBinaryOperator(name,LT,value,isOr);
                    break;
                case LE:
                    temp =handleBinaryOperator(name,LTE,value,isOr);
                    break;
                case IN:
                    temp = new BasicDBObject(IN, value);
                    break;
                case NOT_IN:
                    temp = new BasicDBObject(NIN, value);
                    break;
                case GT:
                    temp = handleBinaryOperator(name,GT,value,isOr);
                    break;
                case GE:
                    temp =handleBinaryOperator(name,GTE,value,isOr);
                    break;
                case IS_NULL:
                    orList.add(new BasicDBObject(name, null));
                    orList.add(new BasicDBObject(name, ""));
                    break;
                default:
            }

            if (operator == Operator.IS_NULL) {
                return this;
            }
            if (isOr) {
                orList.add(new BasicDBObject(name, temp));
            } else {
                andObject.put(name, temp);
            }
            return this;
        }


        /**
         * and 快捷方法
         *
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query and(Operator operator, String name, Object value) {
            return and(operator, name, value, false);
        }

        /**
         * and 操作符string的方式
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query and(String operator, String name, Object value) {
            return and(Operator.getOperator(operator), name, value, false);
        }


        public Query andOverride(Operator operator, String name, Object value){
            this.andObject.put(name,null);
            return and(operator, name, value, false);
        }
        /**
         * or 快捷方法
         *
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query or(Operator operator, String name, Object value) {
            return and(operator, name, value, true);
        }

        /**
         * or 操作符string的方式
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query or(String operator, String name, Object value) {
            return and(Operator.getOperator(operator), name, value, true);
        }

        public Query andEq(String name, Object value) {
            return and(Operator.EQ, name, value);
        }

        public Query andIn(String name, Object value) {
            return and(Operator.IN, name, value);
        }

        public Query andLike(String name, Object value) {
            return and(Operator.LIKE, name, value);
        }

        public Query andGt(String name, Object value) {
            return and(Operator.GT, name, value);
        }

        public Query andGe(String name, Object value) {
            return and(Operator.GE, name, value);
        }

        public Query andLt(String name, Object value) {
            return and(Operator.LT, name, value);
        }

        public Query andLe(String name, Object value) {
            return and(Operator.LE, name, value);
        }

        public Query orEq(String name, Object value) {
            return or(Operator.EQ, name, value);
        }

        public Query orIN(String name, Object value) {
            return or(Operator.IN, name, value);
        }

        public Query orLike(String name, Object value) {
            return or(Operator.LIKE, name, value);
        }

        public Query orGe(String name, Object value) {
            return or(Operator.GE, name, value);
        }

        public Query orGt(String name, Object value) {
            return or(Operator.GT, name, value);
        }

        public Query orLe(String name, Object value) {
            return or(Operator.LE, name, value);
        }

        public Query orLt(String name, Object value) {
            return or(Operator.LT, name, value);
        }

        /**
         * 查询方法 带分页
         *
         * @param tableName 表名称，collection名称
         * @param pageable  分页，为null不分页
         * @return
         */
        public List<Map<String,Object>> query(String tableName, Pageable pageable) {
            if (!orList.isEmpty()) {
                andObject.put(OR, orList);
            }
            LOGGER.info("query:{}", andObject);
            FindIterable<Document> documents = mongoTemplate.getDb().getCollection(tableName).find(andObject).projection(new BasicDBObject().append(OBJECT_ID, 0));
            if (pageable != null) {
                int skip = (int) pageable.getOffset();
                int limit = pageable.getPageSize();
                BasicDBObject sortObject = new BasicDBObject();
                Sort sort = pageable.getSort();
                sort.get().forEach(it -> {
                    sortObject.put(it.getProperty(), it.isAscending() ? 1 : -1);
                });
                documents.sort(sortObject).skip(skip).limit(limit);
            }
            List<Map<String,Object>> result = new ArrayList<>();
            MongoCursor<Document> iterator = documents.iterator();
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
            return result;
        }

        public List<Map<String,Object>> query(String tableName) {
            return query(tableName, null);
        }

        /**
         * 获取总数
         *
         * @param tableName
         * @return
         */
        public long total(String tableName) {
            if (!orList.isEmpty()) {
                andObject.put(OR, orList);
            }
            LOGGER.info("query total:{}", andObject);
            return mongoTemplate.getDb().getCollection(tableName).countDocuments(andObject);
        }

    }

    /**
     * 内部操作因子
     */
    public enum Operator {
        /**
         * EQ 就是 EQUAL等于
         * NE就是 NOT EQUAL不等于
         * GT 就是 GREATER THAN大于
         * LT 就是 LESS THAN小于
         * GE 就是 GREATER THAN OR EQUAL 大于等于
         * LE 就是 LESS THAN OR EQUAL 小于等于
         */
        EQ, NE, LIKE, NOT_LIKE, GT, GE, LT, LE, IN, NOT_IN, IS_NULL;


        /**
         *  根据字符串，返回枚举类型的operator
         * @param operator
         * @return
         */
        public static Operator getOperator(String operator){
            if (StringUtils.isBlank(operator)){
                return EQ;
            }
            switch (operator){
                case "eq":
                    return EQ;
                case "ne":
                    return NE;
                case "like":
                    return LIKE;
                case "not_like":
                    return NOT_LIKE;
                case  "gt":
                    return GT;
                case "ge":
                    return GE;
                case "lt":
                    return LT;
                case "le":
                    return LE;
                case "in":
                    return IN;
                case "not_in":
                    return NOT_IN;
            }
            return EQ;
        }

    }

}