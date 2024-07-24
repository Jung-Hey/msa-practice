package com.example.user_service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // value가 null이면 반환 X
public class ResponseUser {
    private String email;
    private String name;
    private String userId;
    private List<ResponseOrder> orders = new ArrayList<>();
}

