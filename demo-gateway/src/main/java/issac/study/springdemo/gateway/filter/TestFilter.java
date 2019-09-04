package issac.study.springdemo.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author humy6
 */
@Component
public class TestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String suffix = request.getPath().toString();

        System.out.println(request.getPath());
       // URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
    //    String scheme = requestUrl.getScheme();
        if (suffix.equals("/demo-web/websocket")){
            System.out.println("xxxx");
            ServerHttpRequest serverHttpRequest = request.mutate().header("userId", "1@tenant_1")
                    .build();
            ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
            return chain.filter(build);
        }
        return chain.filter(exchange);
    }

    /**
     * 拦截websocket
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
