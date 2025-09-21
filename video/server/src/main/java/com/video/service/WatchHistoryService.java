package com.video.service;

import java.util.List;

import com.video.entity.WatchHistory;

public interface WatchHistoryService {
    void addHistory(Integer userId, Integer movieId, Integer episodeNumber);
    List<WatchHistory> getHistoryMovies(Integer userId);
}