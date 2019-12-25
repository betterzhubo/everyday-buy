package com.seasoning.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRequest {

    @NotBlank
    private String code;


    @NotBlank
    private String name;

}
