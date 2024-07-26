package com.example.user_service.service;

import com.example.user_service.dto.UserDto;
import com.example.user_service.jpa.UserEntity;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.ResponseOrder;
import com.netflix.discovery.converters.Auto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder PasswordEncoder;

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
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders); // 회원의 주문내역도 반환

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
