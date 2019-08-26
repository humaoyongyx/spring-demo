package issac.study.springdemo.core.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author humy6
 * @Date: 2019/8/2 8:36
 */
@Component
@ControllerAdvice(annotations = RestController.class)
public class LogInterceptor implements HandlerInterceptor , ResponseBodyAdvice {

    private static final Logger LOGGER= LoggerFactory.getLogger(LogInterceptor.class);

    private static final String CONTENT_TYPE_JSON="application/json";

    private static final List<String> includeMethods=new ArrayList<>();
    static {
        includeMethods.add("post");
        includeMethods.add("put");
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String methodName="";
        if (handler instanceof HandlerMethod){
            Method method = ((HandlerMethod) handler).getMethod();
            methodName=method.getDeclaringClass().getSimpleName() +"."+method.getName();
        }
        String method = request.getMethod();
        LOGGER.info("request : {} {} [{}]", method,request.getRequestURI(),methodName);
        LOGGER.info("params : {}",getParameters(request));
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.contains(CONTENT_TYPE_JSON)){
           if (includeMethods.contains(method.toLowerCase())){
               String jsonBody = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
               LOGGER.info("params json : \n{}",jsonBody);
           }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {



    }

    private static  String getParameters(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuffer sb=new StringBuffer();
        while (parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            sb.append(key+"->" +value+" ");
        }
        return sb.toString();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
         if (body!=null){
             ObjectMapper mapper = new ObjectMapper();
             try {
                 String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                 LOGGER.info("result json : \n{}", result);
             } catch (JsonProcessingException e) {
                 e.printStackTrace();
             }

         }
        return body;
    }
}
