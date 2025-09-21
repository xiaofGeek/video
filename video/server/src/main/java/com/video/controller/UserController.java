package com.video.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.Result;
import com.video.entity.User;
import com.video.service.UserService;
import com.video.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController // 自动返回数据转换json格式
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result list(){
        List<User> list = userService.list();
        return Result.success(list);
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") long pageNum,
                       @RequestParam(defaultValue = "5") long pageSize,
                       @RequestBody UserVO queryVO) {
        IPage<UserVO> page = new Page<>(pageNum,pageSize);
        IPage<UserVO> list = userService.pageUserByQuery(page,queryVO);
        return Result.success(list.getRecords(), list.getTotal());
    }


    @PostMapping("/add")
    public Result addUser(@RequestBody User user){
        userService.userRegister(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id){
        userService.deleteUserById(id);
        return Result.success();
    }

    @PostMapping("/delBatch")
    public Result deleteBatchUser(@RequestBody List<User> list){
        for (User user : list) {
            userService.deleteUserById(user.getId());
        }
        return Result.success();
    }

    @PostMapping("/edit")
    public Result editUser(@RequestBody User user){
        userService.editUser(user);
        return Result.success();
    }


}
