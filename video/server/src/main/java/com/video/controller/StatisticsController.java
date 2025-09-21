package com.video.controller;

import com.video.common.Result;
import com.video.service.CollectService;
import com.video.service.CommentService;
import com.video.service.MovieService;
import com.video.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CollectService collectService;

    @GetMapping("/overview")
    public Result getOverview() {
        Map<String, Long> statistics = new HashMap<>();
        // 获取用户总数
        statistics.put("userCount", userService.userCount());
        // 获取影片总数
        statistics.put("movieCount", movieService.movieCount());
        // 获取评论总数
        statistics.put("commentCount", commentService.commentCount());
        // 获取收藏总数
        statistics.put("collectCount", collectService.collectCount());

        return Result.success(statistics);
    }
}
