package issac.study.springdemo.web.feign;

import issac.study.springdemo.model.req.PageReq;
import issac.study.springdemo.model.req.UserReq;
import issac.study.springdemo.model.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author humy6
 */
@FeignClient(name = "demo-service",path = "feign")
public interface UserFeignClient {

    @PostMapping("/list")
    List<UserVo> getList(UserReq userReq, @SpringQueryMap PageReq pageReq);

    @GetMapping("/{id}")
    UserVo getById(@PathVariable("id") Integer id);

}
