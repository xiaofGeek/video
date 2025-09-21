package com.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.video.entity.Collect;
import com.video.entity.Movie;
import com.video.mapper.CollectMapper;
import com.video.mapper.MovieMapper;
import com.video.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Override
    @Transactional
    public boolean collect(Integer userId, Integer movieId) {
        if (isCollected(userId, movieId)) {
            return false;
        }
        String movieName = movieMapper.selectById(movieId).getName();

        // 创建收藏记录
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setMovieId(movieId);
        collect.setMovieName(movieName);
        collect.setCreateTime(new Date());
        collectMapper.insert(collect);

        // 更新电影收藏数
        Movie movie = movieMapper.selectById(movieId);
        movie.setCollectCount(movie.getCollectCount() + 1);
        movieMapper.updateById(movie);

        return true;
    }

    @Override
    @Transactional
    public boolean cancelCollect(Integer userId, Integer movieId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("movie_id", movieId);

        if (collectMapper.delete(wrapper) > 0) {
            // 更新电影收藏数
            Movie movie = movieMapper.selectById(movieId);
            movie.setCollectCount(movie.getCollectCount() - 1);
            movieMapper.updateById(movie);
            return true;
        }
        return false;
    }

    @Override
    public List<Collect> getUserCollects(Integer userId) {
        return collectMapper.findMovieIdsByUserId(userId);
    }

    @Override
    public boolean isCollected(Integer userId, Integer movieId) {
        return collectMapper.checkCollect(userId, movieId) > 0;
    }

    @Override
    public Long collectCount() {
        return collectMapper.selectCount(null);
    }
}