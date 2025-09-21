package com.video.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.Result;
import com.video.entity.Comment;
import com.video.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{movieId}")
    public Result getData(@PathVariable Integer movieId){
        List<Comment> list = commentService.findAll(movieId);
        return Result.success(list);
    }

    @PostMapping
    public Result addComment(@RequestBody Comment comment){
        Comment addComment = commentService.addComment(comment);
        return Result.success(addComment);
    }

    @DeleteMapping("/{id}")
    public Result deleteCommentById(@PathVariable Integer id){
        commentService.deleteCommentById(id);
        return Result.success();
    }

    @PostMapping("/delBatch")
    public Result deleteBatchComment(@RequestBody List<Comment> list){
        for (Comment comment : list) {
            commentService.deleteCommentById(comment.getId());
        }
        return Result.success();
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") long pageNum,
                       @RequestParam(defaultValue = "10") long pageSize,
                       @RequestBody Comment query) {
        IPage<Comment> page = new Page<>(pageNum,pageSize);
        IPage<Comment> list = commentService.pageByQuery(page,query);
        return Result.success(list.getRecords(), list.getTotal());
    }

    @PostMapping("/togglePriseOrTread")
    public Result togglePriseOrTread(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        Integer commentId = (Integer) params.get("commentId");
        Boolean isPrise = (Boolean) params.get("isPrise");
        System.out.println("togglePriseOrTread:"+params);
        commentService.togglePriseOrTread(userId, commentId, isPrise);
        return Result.success();
    }
//    @PostMapping("/togglePriseOrTread")
//    public Result togglePriseOrTread(@RequestParam Integer userId, @RequestParam Integer commentId, @RequestParam boolean isPrise) {
//        System.out.println("togglePriseOrTread:"+userId+commentId+isPrise);
//        commentService.togglePriseOrTread(userId, commentId, isPrise);
//        return Result.success();
//    }
}
