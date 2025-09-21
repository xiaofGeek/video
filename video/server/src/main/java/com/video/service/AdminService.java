package com.video.service;

import com.video.entity.Account;
import com.video.entity.Admin;

import java.util.List;

public interface AdminService {
    /**
     * 返回管理员列表
     * @return
     */
    List<Admin> list();

    /**
     * 查找用户名
     * @param account
     * @return
     */
    Account findByAccount(String account);

    /**
     * 管理员登录
     * @param admin
     * @return
     */
    Admin adminLogin(Admin admin);
}
