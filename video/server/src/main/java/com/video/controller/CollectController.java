package com.video.controller;

import com.video.common.Result;
import com.video.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    /**
     * 收藏影视
     * @param movieId
     * @param userId
     * @return
     */
    @PostMapping("/{movieId}")
    public Result collect(@PathVariable Integer movieId, @RequestParam Integer userId) {
        boolean success = collectService.collect(userId, movieId);
        return success ? Result.success() : Result.error("已经收藏过了");
    }

    /**
     * 检查是否已收藏
     * @param movieId
     * @param userId
     * @return
     */
    @GetMapping("/check/{movieId}")
    public Result checkCollect(@PathVariable Integer movieId, @RequestParam Integer userId) {
        boolean isCollected = collectService.isCollected(userId, movieId);
        return Result.success(isCollected);
    }

    @DeleteMapping("/{movieId}")
    public Result cancelCollect(@PathVariable Integer movieId, @RequestParam Integer userId) {
        boolean success = collectService.cancelCollect(userId, movieId);
        return success ? Result.success() : Result.error("取消收藏失败");
    }


    @GetMapping("/user")
    public Result getUserCollects(@RequestParam Integer userId) {
        return Result.success(collectService.getUserCollects(userId));
    }
}