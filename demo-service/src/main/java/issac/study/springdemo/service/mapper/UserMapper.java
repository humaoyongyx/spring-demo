package issac.study.springdemo.service.mapper;

import issac.study.springdemo.service.model.UserBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author humy6
 * @Date: 2019/7/16 12:43
 */
@Repository
public interface UserMapper {

    @Select("select * from user where id=#{id}")
     UserBean get(@Param("id") Integer id);

    void save(UserBean userBean);

}
