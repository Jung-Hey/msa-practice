package com.example.zuul_service.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j // 이거 쓰면 Logger 객체 안만들어도 됨
@Component
public class ZuulLogginFilter extends ZuulFilter {
    @Override
    public Object run() throws ZuulException { // 필터의 동작
        log.info("**********printing pre filter logs: ");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 요청 url 출력
        log.info("**********"+request.getRequestURI());
        return null;
    }

    @Override
    public String filterType() {
        return "pre"; // 사전 필터
    }

    @Override
    public int filterOrder() { // 여러개 필터일떄 순서
        return 1;
    }

    @Override
    public boolean shouldFilter() { // 원하는 옵션에 따라 필터 사용 여부
        return true; // 사용
    }


}
