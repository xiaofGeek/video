package com.video.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.video.common.Result;
import com.video.entity.Category;
import com.video.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result list(){
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    // 获取分类树
    @GetMapping("/tree")
    public Result tree() {
        return Result.success(categoryService.tree());
    }


    // 新增分类
    @PostMapping("/add")
    public Result add(@RequestBody Category category) {
        categoryService.add(category);
        return Result.success();
    }

    // 更新分类
    @PostMapping("/update")
    public Result update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success();
    }

    // 删除分类
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result categoryById(@PathVariable Integer id){
        String name =  categoryService.categoryById(id);
        return Result.success(name);
    }
}
