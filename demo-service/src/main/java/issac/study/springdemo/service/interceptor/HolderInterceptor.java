package issac.study.springdemo.service.interceptor;

import issac.study.springdemo.core.config.cache.DataSourceMapCache;
import issac.study.springdemo.core.config.prop.MultiDataSourceProp;
import issac.study.springdemo.core.context.ContextHolder;
import issac.study.springdemo.core.context.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author humy6
 * @Date: 2019/7/15 17:12
 */

@Component
public class HolderInterceptor implements HandlerInterceptor {

    @Autowired
    MultiDataSourceProp multiDataSourceProp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = User.builder().id(1).dsKey(multiDataSourceProp.getPrimaryKey()).build();

        String dsKey = request.getParameter("dsKey");
        String locale = request.getParameter("locale");
        /**
         * 切换数据源
         */
        if (StringUtils.isNotBlank(dsKey)){
            if (DataSourceMapCache.getDataSourceMap().get(dsKey)!=null){
                user.setDsKey(dsKey);
            }else {
                throw  new  RuntimeException("数据源不存在！");
            }
        }
        if (StringUtils.isNotBlank(locale)){
            if ("en".equals(locale)){
                user.setLocale(Locale.US);
            }
        }
        ContextHolder.setUser(user);
        return true;
    }
}
