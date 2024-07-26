package com.example.user_service.controller;

import com.example.user_service.dto.UserDto;
import com.example.user_service.jpa.UserEntity;
import com.example.user_service.service.UserService;
import com.example.user_service.vo.Greeting;
import com.example.user_service.vo.RequestUser;
import com.example.user_service.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;
    @GetMapping("/health-check")
    public String status(){
        return String.format("It's working in user service on Port : %s",
                env.getProperty("local.server.port")); // 포트 번호 출력
    }
    @GetMapping("/welcome")
    public String welcome(){
        /**
         * yml 파일에 등록된 값 불러오기 방법 1: Environment.getProperty()
         */
        //return env.getProperty("greeting.message");
        /**
         * yml 파일에 등록된 값 불러오기 방법 2: @Value
         */
        return greeting.getMessage();
    }
    /**
     * 회원가입
     */
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);// RequestUser -> UserDto 변환
        userService.createUser(userDto); // createUser -> UserEntity로 변환 후 저장

        // 가입 후 반환 객체 상태코드 + UserResponse 객체
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();
        List<ResponseUser> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        userList.forEach( v -> {
            result.add(mapper.map(v, ResponseUser.class));
        } );
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId ){
        UserDto findUser = userService.getUserByUserId(userId);
        ModelMapper mapper = new ModelMapper();
        ResponseUser result = mapper.map(findUser, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
