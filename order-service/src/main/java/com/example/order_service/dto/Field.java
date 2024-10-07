package com.example.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 데이터 저장을 위한 필드 정보
 */
@Data
@AllArgsConstructor
public class Field {
    private String type;
    private boolean optional;
    private String field;
}
