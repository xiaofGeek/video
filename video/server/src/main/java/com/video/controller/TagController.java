package com.video.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.Result;
import com.video.entity.Tag;
import com.video.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public Result list() {
        return Result.success(tagService.list());
    }

    @PostMapping("/save")
    public Result save(@RequestBody Tag tag) {
        try {
            if (tag.getId() == null) {
                tagService.save(tag);
            } else {
                tagService.updateById(tag);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error("操作失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            tagService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/delBatch")
    public Result deleteBatchTag(@RequestBody List<Tag> list){
        for (Tag tag : list) {
            tagService.removeById(tag.getId());
        }
        return Result.success();
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") long pageNum,
                       @RequestParam(defaultValue = "10") long pageSize,
                       @RequestBody Tag query) {
        IPage<Tag> page = new Page<>(pageNum,pageSize);
        IPage<Tag> list = tagService.pageByQuery(page,query);
        return Result.success(list.getRecords(), list.getTotal());
    }
}
