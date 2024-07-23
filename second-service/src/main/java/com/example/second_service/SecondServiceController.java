package com.example.second_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service")
@Slf4j
public class SecondServiceController {
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the second service";
    }

    @GetMapping("/message")
    //요청헤더 값 출력
    public String message(@RequestHeader("second-request") String header){
        log.info(header);
        return "Hello Second Service Message";
    }
    @GetMapping("/check")
    public String check(){
        return "This message from second check";
    }
}
