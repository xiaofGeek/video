package com.video.vo;

import lombok.Data;

@Data
public class UserVO {
    private int id;
    private String account;
    private String name;
    private String gender;
    private int age;
    private String phone;
    private String email;
    private String createtime;
    private String avatar;
}
