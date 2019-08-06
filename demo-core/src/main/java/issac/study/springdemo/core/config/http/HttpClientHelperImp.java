package issac.study.springdemo.core.config.http;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 基于restTemplate和okHttp3
 *
 * @author humy6
 */
public class HttpClientHelperImp implements HttpClientHelper {

    public static final String QUESTION_MARK = "?";
    public static final String AND_MARK = "&";
    public static final String EQUAL_MARK = "=";
    public static final String SLASH = "/";
    public static final String OPEN_CURLY = "{";
    public static final String CLOSE_CURLY = "}";

    private RestTemplate restTemplate;

    public HttpClientHelperImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String get(String url) {
        return get(url, null, null);
    }

    @Override
    public String get(String url, Map<String, Object> params) {
        return get(url, params, null);
    }

    @Override
    public String get(String url, Map<String, Object> params, Map<String, String> headers) {
        return get(url, params, headers, null);
    }

    @Override
    public String getWithJsonBody(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody) {
        throw new NotImplementedException("okhttp3不支持");
    }

    @Override
    public String get(String url, Map<String, Object> params, Map<String, String> headers, String body) {
        return execute(url, GET, headers, null, params);
    }

    @Override
    public String post(String url, Map<String, Object> params) {
        return post(url, params, null, null);
    }

    @Override
    public String post(String url, Map<String, Object> params, Map<String, String> headers) {
        return post(url, params, headers, null);
    }

    @Override
    public String postWithJsonBody(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", APPLICATION_JSON);
        return post(url, params, headers, jsonBody);
    }

    @Override
    public String post(String url, Map<String, Object> params, Map<String, String> headers, String body) {
        return execute(url, POST, headers, body, params);
    }

    @Override
    public String execute(String url, String method, Map<String, String> headers, String body, Map<String, Object> params) {
        Objects.requireNonNull(url);
        url=url.trim();
        HttpMethod httpMethod = HttpMethod.GET;
        if (StringUtils.isNotBlank(method)) {
            if (POST.equals(method)) {
                httpMethod = HttpMethod.POST;
            } else if (PUT.equals(method)) {
                httpMethod = HttpMethod.PUT;
            } else if (DELETE.equals(method)) {
                httpMethod = HttpMethod.DELETE;
            }
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null && !headers.isEmpty()) {
            headers.entrySet().stream().forEach(it -> {
                httpHeaders.add(it.getKey(), it.getValue());
            });
        }
        HttpEntity httpEntity = new HttpEntity(body, httpHeaders);
        if (params == null) {
            params = new HashMap<>();
        } else {
            url = buildQuery(url, params);
        }
        ResponseEntity<String> exchange = restTemplate.exchange(url, httpMethod, httpEntity, String.class, params);
        return exchange.getBody();
    }

    private String buildQuery(String baseUrl, Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            if (baseUrl.contains(QUESTION_MARK)) {
                sb.append(AND_MARK);
            } else {
                sb.append(QUESTION_MARK);
            }
            if (baseUrl.endsWith(SLASH)) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            for (Map.Entry<String, Object> item : params.entrySet()) {
                if (item.getValue()!=null){
                    sb.append(item.getKey() + EQUAL_MARK + OPEN_CURLY + item.getKey() + CLOSE_CURLY + AND_MARK);
                }
            }
            sb = sb.replace(sb.length() - 1, sb.length(), "");
        }
        return baseUrl + sb.toString();
    }

}
