package com.example.first_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class FirstServiceController {
    Environment env;
    @Autowired
    public FirstServiceController(Environment env) {
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the first service";
    }

    @GetMapping("/message")
    //요청헤더 값 출력
    public String message(@RequestHeader("first-request") String header){
        log.info(header);
        return "Hello First Service Message";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("server port = {} ",request.getServerPort());
        return String.format("This message from first check, Port is %s",
                env.getProperty("local.server.port"));
    }
}
