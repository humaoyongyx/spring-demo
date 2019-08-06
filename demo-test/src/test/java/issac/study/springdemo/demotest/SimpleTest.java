package issac.study.springdemo.demotest;

import com.alibaba.fastjson.JSON;
import issac.study.springdemo.core.config.http.HttpClientHelper;
import issac.study.springdemo.core.config.http.HttpClientHelperImp;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author humy6
 * @Date: 2019/8/5 16:33
 */
public class SimpleTest {

    private Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    private HttpClientHelper httpClientHelper = null;

    @Before
    public void init() throws NoSuchAlgorithmException, KeyManagementException {


        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                System.out.println(hostname);
                return true;
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        ConnectionPool connectionPool = new ConnectionPool(50, 5, TimeUnit.MINUTES);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                logger.debug(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient     okHttpClient=  new OkHttpClient.Builder()
               .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                .hostnameVerifier(DO_NOT_VERIFY)
                .retryOnConnectionFailure(false)
                .connectionPool(connectionPool)//连接池
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS)
                .writeTimeout(10L,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();


        httpClientHelper = new HttpClientHelperImp(new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient)));
    }

    @Test
    public void testGet() {

        Map<String, String> headers = new HashMap<>();
        headers.put("mroToken", "test");
        Map<String, Object> params = new HashMap<>();
        params.put("value", "http://localhost:8080/1?id=3");
        Map map = new HashMap();
        map.put("equipmentTypeId", 1);
        String result = httpClientHelper.getWithJsonBody("http://localhost:8080/mro-base/equipment/search?value1=测试&size=1000", params, headers,JSON.toJSONString(map));
        System.out.println(result);
    }

    @Test
    public void testPost() {

        Map<String, String> headers = new HashMap<>();
        headers.put("mroToken", "test");
        Map<String, Object> params = new HashMap<>();
        params.put("size", 1);
        Map map = new HashMap();
        map.put("equipmentTypeId", 1);
        String result = httpClientHelper.postWithJsonBody("http://localhost:8080/mro-base/equipment/page", params, headers, JSON.toJSONString(map));
        System.out.println(result);
    }

    @Test
    public void testHttps() {
        String result = httpClientHelper.get("https://localhost");
        System.out.println(result);
    }
}
