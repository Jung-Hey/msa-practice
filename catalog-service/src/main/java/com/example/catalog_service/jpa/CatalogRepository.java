package com.example.catalog_service.jpa;

import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
    // 메소드 이름으로 쿼리 생성
    CatalogEntity findByProductId(String productId);
}
