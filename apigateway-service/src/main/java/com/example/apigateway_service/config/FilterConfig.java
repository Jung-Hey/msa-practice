package com.example.apigateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
  //  @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                //라우팅 설정
                .route( r -> r.path("/first-service/**")
                             // 요청헤더 응답해더 생성
                             .filters( f-> f.addRequestHeader("first-request","first-request-header")
                                            .addResponseHeader("first-response","first-response-header"))
                            // 이동할 uri
                             .uri("http://localhost:8081")
                        )
                .route( r -> r.path("/second-service/**")
                        .filters( f-> f.addRequestHeader("second-request","second-request-header")
                                       .addResponseHeader("second-response","second-response-header"))
                        .uri("http://localhost:8082")
                )
                .build();
    }
}
