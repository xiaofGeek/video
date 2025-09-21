package com.video.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.video.common.Result;
import com.video.entity.WatchHistory;
import com.video.service.WatchHistoryService;

@RestController
@RequestMapping("/history")
@CrossOrigin
public class WatchHistoryController {
    @Autowired
    private WatchHistoryService watchHistoryService;

    @PostMapping("/add")
    public Result addHistory(@RequestParam Integer userId, @RequestParam Integer movieId,@RequestParam Integer episodeNumber) {
        watchHistoryService.addHistory(userId, movieId,episodeNumber);
        return Result.success();
    }

    @GetMapping("/list")
    public Result getHistoryMovies(@RequestParam Integer userId) {
        System.out.println("接收到获取观看记录请求，用户ID: " + userId);
        List<WatchHistory> historyList = watchHistoryService.getHistoryMovies(userId);
        System.out.println("查询结果数量: " + (historyList != null ? historyList.size() : "null"));
        if (historyList != null) {
            historyList.forEach(history -> {
                System.out.println("记录详情: " + 
                    "ID=" + history.getId() + 
                    ", 电影ID=" + history.getMovieId() + 
                    ", 电影名=" + history.getMovieName() + 
                    ", 集数=" + history.getLastEpisode());
            });
        }
        return Result.success(historyList);
    }
}