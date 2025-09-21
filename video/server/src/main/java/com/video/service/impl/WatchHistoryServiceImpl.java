package com.video.service.impl;

import java.util.Date;
import java.util.List;

import com.video.entity.Movie;
import com.video.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.entity.WatchHistory;
import com.video.mapper.WatchHistoryMapper;
import com.video.service.WatchHistoryService;

@Service
public class WatchHistoryServiceImpl implements WatchHistoryService {
    @Autowired
    private WatchHistoryMapper watchHistoryMapper;
    @Autowired
    private MovieMapper movieMapper;


    @Override
    public void addHistory(Integer userId, Integer movieId, Integer episodeNumber) {
        // 先查询是否已存在该记录
        LambdaQueryWrapper<WatchHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WatchHistory::getUserId, userId)
                .eq(WatchHistory::getMovieId, movieId);
        WatchHistory existHistory = watchHistoryMapper.selectOne(wrapper);
        Movie movie = movieMapper.selectById(movieId);

        if (existHistory != null) {
            // 如果存在，更新观看时间和集数
            existHistory.setWatchTime(new Date());
            existHistory.setMovieName(movie.getName());
            existHistory.setLastEpisode(episodeNumber);
            watchHistoryMapper.updateById(existHistory);
        } else {
            // 不存在则新增
            WatchHistory history = new WatchHistory();
            // LambdaQueryWrapper<Movie> queryWrapper = new LambdaQueryWrapper<>();
            // queryWrapper.eq(Movie::getId, movieId);
            

            history.setUserId(userId);
            history.setMovieId(movieId);
            history.setMovieName(movie.getName());
            history.setLastEpisode(episodeNumber);
            history.setWatchTime(new Date());
            watchHistoryMapper.insert(history);
        }
    }

    @Override
    public List<WatchHistory> getHistoryMovies(Integer userId) {
        return watchHistoryMapper.getHistoryMovies(userId);
    }
}