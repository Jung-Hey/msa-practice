package com.example.user_service.service;

import com.example.user_service.client.OrderServiceClient;
import com.example.user_service.dto.UserDto;
import com.example.user_service.jpa.UserEntity;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.ResponseOrder;
import com.netflix.discovery.converters.Auto;
import feign.FeignException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder PasswordEncoder;

    private final Environment env;
    private final RestTemplate restTemplate;

    private final OrderServiceClient orderServiceClient;


    @Override
    // DB에서 레포지토리 값을 가지고 검사하는 부분
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if( userEntity == null )
            throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true,true,true,true, new ArrayList<>());
        //ArrayList 에는 로그인 성공 시 부여할 권한을 넣으면 됨
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        // MatchingStrategies.STRICT : 딱 맞아떨어지지 않으면 지정 불
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);// 변환

        //userEntity.setEncryptedPwd("encrypted_password");
        userEntity.setEncryptedPwd(PasswordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);
        // 반환
        return mapper.map(userEntity, UserDto.class);
    }

    @Override // 회원 정보 + 회원의 주문정보 반환
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if( userEntity == null )
            throw new UsernameNotFoundException("User not found");
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        //List<ResponseOrder> orders = new ArrayList<>();
        //String orderUrl = "http://127.0.0.1:8000/order-service/%s/orders";

        // 방법1 RestTemplate : 유저서비스에서 주문 서비스 호출
        /*
        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
        ResponseEntity<List<ResponseOrder>> orderListResponse =
                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<ResponseOrder>>() {
                });
        List<ResponseOrder> ordersList = orderListResponse.getBody();
         */

        // 방법2 Feign Client + 예외처리
//        List<ResponseOrder> ordersList = null;
//        try {
//            ordersList = orderServiceClient.getOrders(userId);
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }
        //예외처리 에러 디코더로 함
        List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId);

        userDto.setOrders(ordersList); // 회원의 주문내역도 반환

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return  userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if( userEntity==null )
            throw new UsernameNotFoundException(email);
       // UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userEntity, UserDto.class);

        return userDto;
    }
}
