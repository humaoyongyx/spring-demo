package issac.study.springdemo.core.context;

import java.util.Optional;

/**
 * @author humy6
 * @Date: 2019/7/16 10:19
 */
public class ContextHolder {

    private static ThreadLocal<User> threadLocal=new ThreadLocal<>();

    public static void setUser(User user){
         threadLocal.set(user);
    }

    public static Optional<User> getUser(){
        return Optional.ofNullable(threadLocal.get());
    }

}
