package issac.study.springdemo.service.repository;

import issac.study.springdemo.service.model.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author humy6
 * @Date: 2019/7/16 13:03
 */
public interface UserRepository extends JpaRepository<UserBean,Integer> {
}
