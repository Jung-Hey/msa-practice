package com.example.apigateway_service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public CustomFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        // 커스텀 Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("PRE FILTER request.getId -> {}",request.getId());
            //커스텀 POST FILTER
            return chain.filter(exchange).then(Mono.fromRunnable(()-> {
                log.info("POST FILTER response code -> {}",response.getStatusCode());
            }));
        };
    }
    public static class Config{
        // 설정 정보
     }
}
