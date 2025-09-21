package com.video.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String account;
    private String password;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private int age;
}
