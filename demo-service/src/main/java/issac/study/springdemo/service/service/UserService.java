package issac.study.springdemo.service.service;

import issac.study.springdemo.service.mapper.UserMapper;
import issac.study.springdemo.service.model.UserBean;
import issac.study.springdemo.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author humy6
 * @Date: 2019/7/16 13:07
 */

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public void saveJpa(){
        userRepository.save(new UserBean.Builder().name(Math.random()+"name").build());
    }


    public void updateJpa(Integer id){
        userRepository.save(new UserBean.Builder().id(id).name(Math.random()+"name").build());
    }

    /**
     * jpa事务
     */
    @Transactional
    public void saveJpaErr(){
        userRepository.save(new UserBean.Builder().name(Math.random()+"name").build());
        if (true){
            throw new RuntimeException("jpa 事务");
        }
    }

    public void saveMybatis(){
         userMapper.save(new UserBean.Builder().name(Math.random()+"name").build());
    }


    @Transactional("mybatisTransactionManager")
    public void saveMybatisErr(){
        userMapper.save(new UserBean.Builder().name(Math.random()+"name").build());
        if (true){
            throw new RuntimeException("mybatis 事务");
        }
    }

    public UserBean get(Integer id){
           return userMapper.get(id);
    }

}
