package com.video.service;

import com.video.entity.Collect;

import java.util.List;

public interface CollectService {
    boolean collect(Integer userId, Integer movieId);
    boolean cancelCollect(Integer userId, Integer movieId);
    List<Collect> getUserCollects(Integer userId);
    boolean isCollected(Integer userId, Integer movieId);

    /**
     * 统计影片收藏总数
     * @return
     */
    Long collectCount();
}