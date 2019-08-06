package issac.study.springdemo.core.config.http;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 设置 restTemplate为httpclient的实现
 * 设置httpClientHelper工具类
 * @author humy6
 */
@Configuration
public class HttpConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConfig.class);

    @Bean
    public RestTemplate restTemplate(OkHttpClient okHttpClient) {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
    }

    @Bean
    public HttpClientHelper httpClientHelper(RestTemplate restTemplate){
        return new HttpClientHelperImp(restTemplate);
    }

    @Bean
    OkHttpClient okHttpClient() throws Exception {

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

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        ConnectionPool connectionPool = new ConnectionPool(50, 5, TimeUnit.MINUTES);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(LOGGER::debug);
        //打印请求链路，debug级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                //设置https请求不校验证书
                .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                .hostnameVerifier((s, sslSession) -> true)
                .retryOnConnectionFailure(false)
                //连接池设置
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                //连接时间设置
                .connectTimeout(5L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                //请求日志打印
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

}
