package com.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.common.ResultCode;
import com.video.entity.Account;
import com.video.entity.Admin;
import com.video.exception.CustomException;
import com.video.mapper.AdminMapper;
import com.video.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> list() {
        return adminMapper.selectList(null);
    }

    @Override
    public Account findByAccount(String account) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAccount,account);
        return adminMapper.selectOne(queryWrapper);
    }

    @Override
    public Admin adminLogin(Admin admin) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAccount,admin.getAccount());
        queryWrapper.eq(Admin::getPassword,admin.getPassword());
        if(adminMapper.selectList(queryWrapper).size() == 0){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return adminMapper.selectOne(queryWrapper);
    }
}
