package issac.study.springdemo.core.utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

/**
 *
 * @author humy6
 * @Date: 2019/7/10 10:47
 */
public interface RestrictionSpecificationUtil<E> extends Specification<E>, Consumer<RestrictionUtil.Query> {

    /**
     *  Restriction类的接口补充，可以获取Specification类
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @return
     */
    @Override
    default Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        RestrictionUtil.Query<E> query = new RestrictionUtil().buildSpecification(root, criteriaQuery, criteriaBuilder);
        criteriaQuery.distinct(true);
        accept(query);
        return query.restrictions();
    }


}
