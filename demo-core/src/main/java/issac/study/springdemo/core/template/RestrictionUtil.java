package issac.study.springdemo.core.template;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import java.time.ZoneId;
import java.util.*;

/**
 * jpa动态查询工具类
 *
 * @author humy6
 * @Date: 2019/7/9 14:04
 */

@Component
public class RestrictionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestrictionUtil.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    /**
     * 构建Query类且自动注入entityManagerFactory，结合query()方法做最后查询，此类不可new必须，注入到容器
     * @param entityClass 实体类
     * @param <E>
     * @return
     */
    public <E> Query<E> build(Class<E> entityClass) {
        return new Query(entityManagerFactory.createEntityManager(), entityClass);
    }

    /**
     *构建Specification的查询类，结合predicates()方法获取predicate，此类可以直接new
     * 快捷类接口 RestrictionSpecificationUtil 可以使用lambda表达式
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param <E>
     * @return
     */
    public <E> Query<E> buildSpecification(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return new Query(root,criteriaQuery,criteriaBuilder);
    }


    /**
     * 查询内部类
     *
     * @param <E>
     */
    public static class Query<E> {

        private EntityManager entityManager;

        private CriteriaBuilder criteriaBuilder;

        private Root<E> root;

        private CriteriaQuery<E> criteriaQuery;
        private Predicate predicateAnd;
        private Predicate predicateOr;
        private boolean isOr;

        private Map<String, Join> joinMap = new HashMap<>();

        public Query(EntityManager entityManager, Class<E> entityClass) {
            this.entityManager = entityManager;
            this.criteriaBuilder = entityManager.getCriteriaBuilder();
            this.criteriaQuery = this.criteriaBuilder.createQuery(entityClass);
            this.root = this.criteriaQuery.from(entityClass);
            this.predicateAnd=this.criteriaBuilder.conjunction();
            this.predicateOr=this.criteriaBuilder.disjunction();
        }

        public Query(Root<E> root, CriteriaQuery<E> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            this.criteriaBuilder = criteriaBuilder;
            this.criteriaQuery = criteriaQuery;
            this.root = root;
            this.predicateAnd=this.criteriaBuilder.conjunction();
            this.predicateOr=this.criteriaBuilder.disjunction();
        }


        /**
         * 开启or查询
         * 默认是and连接操作 此时如果没有任何条件参数，默认返回所有列表
         * or查询开启之后and和or是并集查询，此时没有任何参数返回的是空列表
         */
        public Query<E> enableOr(){
             this.isOr=true;
             return  this;
        }

        /**
         * 关联表 注意：下面的关联查询必须先在这里声明才可以查询
         *
         * @param joinName
         * @param joinType
         * @return
         */
        public Query<E> join(String joinName, JoinType joinType) {
            joinMap.put(joinName, root.join(joinName, joinType));
            return this;
        }


        private Query<E> check(String joinName, Operator operator, String name, Object value){
            if (value == null) {
                if (operator!= Operator.IS_NULL){
                    return this;
                }
            }
            if (value instanceof String){
                if (StringUtils.isBlank((String) value)) {
                    return this;
                }
            }
            if (value instanceof Collection){
                if (((Collection)value).isEmpty()){
                    return  this;
                }
            }
            if (operator== Operator.GE||operator== Operator.GT||operator== Operator.LE||operator== Operator.LT){
                if (!(value instanceof Comparable)){
                    return  this;
                }
            }
            return null;
        }

        private  Object resetValue(String joinName, Operator operator, String name, Object value){
            if (operator== Operator.LIKE || operator== Operator.NOT_LIKE){
                String tempVal=(String)value;
                if (tempVal.indexOf("%")<0){
                    value=tempVal.trim()+"%";
                }
            }
            if (operator== Operator.GT||operator== Operator.GE||operator== Operator.LE||operator== Operator.LT){
                if (value instanceof Date){
                  value= (((Date) value).toInstant().atZone(ZoneId.systemDefault())).toLocalDateTime();
                }
            }
            return value;
        }

        private  Path getPath(String joinName, Operator operator, String name, Object value){
            Path path ;
            if (joinName == null) {
                path = root.get(name);
            } else {
                Objects.requireNonNull(joinName);
                Join join = joinMap.get(joinName);
                Objects.requireNonNull(join);
                path = join.get(name);
            }
            return path;
        }
        /**
         * 关联表查询功能
         *
         * @param joinName 关联表的在主表的字段名称，主表必须实现关联（如OneToOne）
         * @param operator 操作因子 如 = >
         * @param name     查询的实体字段名称
         * @param value    查询的值
         * @return
         */
        public Query<E> and(String joinName, Operator operator, String name, Object value) {

            Query<E> check = check(joinName, operator, name, value);
            if (check !=null){
                return  check;
            }
            value= resetValue(joinName, operator, name, value);
            Path path = getPath(joinName, operator, name, value);
            switch (operator) {
                case EQ:
                        predicateAnd=criteriaBuilder.and(predicateAnd,criteriaBuilder.equal(path, value));
                    break;
                case GE:
                    predicateAnd=criteriaBuilder.and(predicateAnd,criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) value));
                    break;
                case GT:
                    predicateAnd=criteriaBuilder.and(predicateAnd,criteriaBuilder.greaterThan(path, (Comparable) value));
                    break;
                case IN:
                    Collection collection = (Collection) value;
                        if (collection.size()==1){
                            predicateAnd=criteriaBuilder.and(predicateAnd,criteriaBuilder.equal(path, collection.iterator().next()));
                        }else {
                            predicateAnd=criteriaBuilder.and(predicateAnd,path.in(collection));
                        }
                    break;
                case LE:
                    predicateAnd=criteriaBuilder.and(predicateAnd,criteriaBuilder.lessThanOrEqualTo(path, (Comparable)  value));
                    break;
                case LT:
                    predicateAnd= criteriaBuilder.and(predicateAnd,criteriaBuilder.lessThan(path, (Comparable) value));
                    break;
                case NE:
                    predicateAnd= criteriaBuilder.and(predicateAnd,criteriaBuilder.notEqual(path, value));
                    break;
                case LIKE:
                    predicateAnd= criteriaBuilder.and(predicateAnd,criteriaBuilder.like(path, (String) value));
                    break;
                case NOT_LIKE:
                    predicateAnd= criteriaBuilder.and(predicateAnd,criteriaBuilder.notLike(path, (String) value));
                    break;
                case IS_NULL:
                    predicateAnd=criteriaBuilder.and(predicateAnd,path.isNull());
                    break;
            }
            return this;
        }

        /**
         * 关联表查询功能
         *
         * @param joinName 关联表的在主表的字段名称，主表必须实现关联（如OneToOne）
         * @param operator 操作因子 如 = >
         * @param name     查询的实体字段名称
         * @param value    查询的值
         * @return
         */
        public Query<E> or(String joinName, Operator operator, String name, Object value) {

            Query<E> check = check(joinName, operator, name, value);
            if (check !=null){
                return  check;
            }
            value= resetValue(joinName, operator, name, value);
            Path path = getPath(joinName, operator, name, value);

            switch (operator) {
                case EQ:
                    predicateOr=  criteriaBuilder.or(predicateOr,criteriaBuilder.equal(path, value));
                    break;
                case GE:
                    predicateOr=criteriaBuilder.or(predicateOr,criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) value));
                    break;
                case GT:
                    predicateOr=criteriaBuilder.or(predicateOr,criteriaBuilder.greaterThan(path, (Comparable) value));
                    break;
                case IN:
                    Collection collection = (Collection) value;
                    if (collection.size()==1){
                        predicateOr=criteriaBuilder.or(predicateOr,criteriaBuilder.equal(path, collection.iterator().next()));
                    }else {
                        predicateOr= criteriaBuilder.or(predicateOr,path.in(collection));
                    }
                    break;
                case LE:
                    predicateOr=criteriaBuilder.or(predicateOr,criteriaBuilder.lessThanOrEqualTo(path, (Comparable) value));
                    break;
                case LT:
                    predicateOr= criteriaBuilder.or(predicateOr,criteriaBuilder.lessThan(path, (Comparable) value));
                    break;
                case NE:
                    predicateOr=criteriaBuilder.or(predicateOr,criteriaBuilder.notEqual(path, value));
                    break;
                case LIKE:
                    predicateOr=criteriaBuilder.or(predicateOr,criteriaBuilder.like(path, (String) value));
                    break;
                case NOT_LIKE:
                    predicateOr=criteriaBuilder.or(predicateOr,criteriaBuilder.notLike(path, (String) value));
                    break;
                case IS_NULL:
                    predicateOr=criteriaBuilder.or(predicateOr,path.isNull());
                    break;
            }
            return this;
        }

        /**
         * 主表查询的快捷方式
         *
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query<E> and(Operator operator, String name, Object value) {
            return and(null, operator, name, value);
        }


        /**
         *  equal关联表查询的快捷方式
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andEq(String joinName,String name, Object value) {
            return and(joinName, Operator.EQ, name, value);
        }

        /**
         * 操作符为Eq的主表查询快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andEq(String name, Object value) {
            return andEq(null, name, value);
        }



        /**
         *  in关联表查询的快捷方式
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andIn(String joinName,String name, Object value) {
            return and(joinName, Operator.IN, name, value);
        }

        /**
         *in主表查询的快捷方式
         * @param name
         * @param value
         * @return
         */
        public Query<E> andIn(String name, Object value) {
            return andIn(null, name, value);
        }

        /**
         *  like关联表查询的快捷方式
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLike(String joinName,String name, Object value) {
            return and(joinName, Operator.LIKE, name, value);
        }

        /**
         * like主表查询的快捷方式
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLike(String name, Object value) {
            return andLike(null,  name, value);
        }


        /**
         * 关联表id查询的快捷方式
         * @param joinName
         * @param value
         * @return
         */
        public Query<E> andEqId(String joinName,Object value) {
            return andEq(joinName, "id", value);
        }

        /**
         * 操作符为Eq且查询字段为id的主表查询快捷方式
         *
         * @param value
         * @return
         */
        public Query<E> andEqId(Object value) {
            return andEqId(null, value);
        }


        /**
         * 关联表ids列表查询的快捷方式
         * @param joinName
         * @param ids
         * @return
         */
        public Query<E> andInIds(String joinName,Collection ids) {
            return andIn(joinName, "id", ids);
        }

        /**
         * 操作符为in且查询字段为id的list查询快捷方式
         *
         * @param ids
         * @return
         */
        public Query<E> andInIds(Collection ids) {
            return andInIds(null,  ids);
        }


        /**
         * 主表查询的快捷方式
         *
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query<E> or(Operator operator, String name, Object value) {
            return or(null, operator, name, value);
        }


        /**
         *  equal关联表查询的快捷方式
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> orEq(String joinName,String name, Object value) {
            return or(joinName, Operator.EQ, name, value);
        }

        /**
         * 操作符为Eq的主表查询快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> orEq(String name, Object value) {
            return orEq(null,  name, value);
        }



        /**
         *  in关联表查询的快捷方式
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> orIn(String joinName,String name, Object value) {
            return or(joinName, Operator.IN, name, value);
        }

        /**
         *in主表查询的快捷方式
         * @param name
         * @param value
         * @return
         */
        public Query<E> orIn(String name, Object value) {
            return orIn(null, name, value);
        }

        /**
         *  like关联表查询的快捷方式
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> orLike(String joinName,String name, Object value) {
            return or(joinName, Operator.LIKE, name, value);
        }

        /**
         * like主表查询的快捷方式
         * @param name
         * @param value
         * @return
         */
        public Query<E> orLike(String name, Object value) {
            return orLike(null, name, value);
        }


        /**
         * 关联表id查询的快捷方式
         * @param joinName
         * @param value
         * @return
         */
        public Query<E> orEqId(String joinName,Object value) {
            return orEq(joinName,"id",value);
        }


        /**
         * 操作符为Eq且查询字段为id的主表查询快捷方式
         *
         * @param value
         * @return
         */
        public Query<E> orEqId(Object value) {
            return orEqId(null, value);
        }


        /**
         * 关联表ids列表查询的快捷方式
         * @param joinName
         * @param ids
         * @return
         */
        public Query<E> orInIds(String joinName,Collection ids) {
            return orIn(joinName,  "id", ids);
        }

        /**
         * 操作符为in且查询字段为id的list查询快捷方式
         *
         * @param ids
         * @return
         */
        public Query<E> orInIds(Collection ids) {
            return orInIds(null, ids);
        }

        /**
         * 配合 build(Class<E> entityClass)  查询
         * @return
         */
        public List<E> query() {
            List<E> resultList = new ArrayList<>();
            Predicate restrictions ;
            if (this.isOr){
                restrictions=this.criteriaBuilder.and(this.predicateAnd,this.predicateOr);
            }else {
                restrictions=this.criteriaBuilder.and(this.predicateAnd);
            }

            try {
                CriteriaQuery<E> query = this.criteriaQuery.where(restrictions);
                resultList = this.entityManager.createQuery(query).getResultList();

            } catch (Exception e) {
                LOGGER.error("query error:", e);
            } finally {
                if (this.entityManager != null) {
                    this.entityManager.close();
                }
            }
            return resultList;
        }

        /**
         * 配合buildSpecification(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) ，用于产生specification类
         * @return
         */
       public Predicate restrictions(){
           Predicate restrictions ;
           if (this.isOr){
               restrictions=this.criteriaBuilder.and(this.predicateAnd,this.predicateOr);
           }else {
               restrictions=this.criteriaBuilder.and(this.predicateAnd);
           }
            return restrictions;
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
        EQ, NE, LIKE, NOT_LIKE, GT, GE, LT, LE, IN, IS_NULL;

    }

}
