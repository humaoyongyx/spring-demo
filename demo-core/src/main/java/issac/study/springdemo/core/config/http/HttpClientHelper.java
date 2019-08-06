package issac.study.springdemo.core.config.http;

import java.util.Map;

/**
 *
 *http 通用请求接口工具类
 * @author humy6
 * @Date: 2019/8/5 16:06
 */
public interface HttpClientHelper {

    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";
    String APPLICATION_JSON="application/json;charset=UTF-8";
    String APPLICATION_DEFAULT="application/x-www-form-urlencoded";
    /**
     * GET 快捷请求
     * @param url
     * @return
     */
    String get(String url);

    /**
     * GET 带参数的快捷请求
     * @param url
     * @param params
     * @return
     */
    String get(String url, Map<String, Object> params);

    /**
     * GET 复杂的快捷请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    String get(String url, Map<String, Object> params, Map<String, String> headers);

    /**
     * POST 带参数的快捷请求
     * @param url
     * @param params
     * @return
     */
    String post(String url, Map<String, Object> params);

    /**
     * POST 复杂的快捷请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    String post(String url, Map<String, Object> params, Map<String, String> headers);

    /**
     * POST  Content-Type application/json;charset=utf-8
     * @param url
     * @param params
     * @param headers
     * @param jsonBody
     * @return
     */
    String postWithJsonBody(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody);

    /**
     * GET  Content-Type application/json;charset=utf-8
     * @param url
     * @param params
     * @param headers
     * @param jsonBody
     * @return
     */
    String getWithJsonBody(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody);


    /**
     * POST请求的通用请求方法
     * @param url 请求基础url
     * @param params 请求Query参数
     * @param headers 请求headers
     * @param body  请求body体，底层的某些http框架可能不支持
     * @return
     */
    String post(String url, Map<String, Object> params, Map<String, String> headers, String body);

    /**
     * GET请求的通用请求方法
     * @param url 请求基础url
     * @param params 请求Query参数
     * @param headers 请求headers
     * @param body  请求body体，底层的某些http框架可能不支持
     * @return
     */
    String get(String url, Map<String, Object> params, Map<String, String> headers, String body);

    /**
     * 通用执行请求方法
     * 默认为：Content-Type application/www-url-encode 也及key-value形式
     * @param url  请求基础url
     * @param method  请求方法名称
     * @param headers  请求headers
     * @param body       请求body体
     * @param params   请求Query参数
     * @return   返回String类型
     */
    String execute(String url, String method, Map<String, String> headers, String body, Map<String, Object> params);
}
