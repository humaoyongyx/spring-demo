package issac.study.springdemo.core.filter;

import issac.study.springdemo.core.wrapper.JsonRequestWrapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hmy
 */
public class JsonRequestFilter implements Filter {

    private static final String CONTENT_TYPE_JSON="application/json";
    private static final List<String> includeMethods=new ArrayList<>();
    static {
        includeMethods.add("post");
        includeMethods.add("put");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String contentType = request.getContentType();
            if (StringUtils.isNotBlank(contentType) && contentType.contains(CONTENT_TYPE_JSON)){
                String method = ((HttpServletRequest) request).getMethod();
                    if (includeMethods.contains(method.toLowerCase())){
                        ServletRequest requestWrapper = new JsonRequestWrapper((HttpServletRequest) request);
                        chain.doFilter(requestWrapper, response);
                        return;
                    }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
    
}