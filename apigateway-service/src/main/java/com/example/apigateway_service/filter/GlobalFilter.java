package com.example.apigateway_service.filter;

import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public GlobalFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        // 커스텀 Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("GlOBAL FILTER baseMessage -> {}",config.getBaseMessage());
            if(config.isPreLogger()){
                log.info("GlOBAL FILTER start : request id -> {}",request.getId());
            }
            //커스텀 POST FILTER
            return chain.filter(exchange).then(Mono.fromRunnable(()-> {
                if(config.isPostLogger()){
                    log.info("GlOBAL FILTER end : response code -> {}",response.getStatusCode());
                }
            }));
        };
    }
    @Data
    public static class Config{
        // 설정 정보
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
     }
}
