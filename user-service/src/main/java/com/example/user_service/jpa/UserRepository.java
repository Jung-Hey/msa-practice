package com.example.user_service.jpa;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Long> {

    // 메소드 이름으로 쿼리 생성
    UserEntity findByUserId(String userId);
}
