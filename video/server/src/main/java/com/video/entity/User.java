package com.video.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User extends Account{
    @TableField("name")
    private String name;
    @TableField("gender")
    private String gender;
    @TableField("phone")
    private String phone;
    @TableField("email")
    private String email;
    @TableField("age")
    private int age;
    @TableField("avatar")
    private String avatar;
}
