package com.video.controller;

import com.video.common.Result;
import com.video.common.config.JwtTokenUtils;
import com.video.dto.UserRegisterDTO;
import com.video.entity.Account;
import com.video.entity.Admin;
import com.video.entity.User;
import com.video.service.AdminService;
import com.video.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@CrossOrigin //解决跨域
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    /**
     * 注册账户
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    public Result accountRegister(@RequestBody UserRegisterDTO userRegisterDTO){
        // 将DTO转换为实体类
        User user = new User();
        user.setAccount(userRegisterDTO.getAccount());
        user.setPassword(userRegisterDTO.getPassword());
        user.setName(userRegisterDTO.getName());
        user.setGender(userRegisterDTO.getGender());
        user.setPhone(userRegisterDTO.getPhone());
        user.setEmail(userRegisterDTO.getEmail());
        user.setAge(userRegisterDTO.getAge());

        // 设置默认值
        user.setRole(2); // 默认设置为普通用户角色
        user.setCreatetime(new Date()); // 设置创建时间为当前时间
        user.setAvatar("https://www.keaitupian.cn/cjpic/frombd/0/253/1152107840/119779555.jpg");
        User userRegister = userService.userRegister(user);
        return Result.success(userRegister);
    }

    /**
     * 用户登录
     * @param account
     * @return
     */
    @PostMapping("/login")
    public Result accountLogin(@RequestBody Account account){
        Integer role = account.getRole();
        Account login_account = new Account();

        if(role == 1){
            Admin admin = new Admin();
            BeanUtils.copyProperties(account,admin);
            login_account = adminService.adminLogin(admin);
        }
        if(role == 2){
            User user = new User();
            BeanUtils.copyProperties(account,user);
            System.out.println(user);
            login_account = userService.userLogin(user);
        }
        // 生成token
        String token = JwtTokenUtils.genToken(login_account.getAccount() + "-" + login_account.getRole(), login_account.getPassword());
        // 创建一个键值对map集合，将token和user返回给前端
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("user",login_account);

        return Result.success(map);
    }



}
