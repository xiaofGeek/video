package com.video.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.aliyun.oss.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.common.ResultCode;
import com.video.common.VodTemplate;
import com.video.entity.Category;
import com.video.entity.Movie;
import com.video.exception.CustomException;
import com.video.mapper.CategoryMapper;
import com.video.mapper.MovieMapper;
import com.video.service.MovieService;

import cn.hutool.core.util.ObjectUtil;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private VodTemplate vodTemplate;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 查询影视列表
     * @return
     */
    @Override
    public List<Movie> list(Integer categoryId) {
        if (categoryId == null) {
            return movieMapper.selectList(null);
        }
        return movieMapper.selectByCategory(categoryId);
    }
    /**
     * 新增影视
     * @param movie
     */
    @Override
    public Movie saveMovie(Movie movie) {
        movieMapper.insert(movie);
        return movie;
    }

    @Override
    public Movie update(Movie movie) {
        movieMapper.updateById(movie);
        return movie;
    }

    /**
     * 根据影视id查询影视详情
     * @param id
     * @return
     */
    @Override
    public Movie findById(Integer id) {
        return movieMapper.selectById(id);
    }

    /**
     * 根据影视id删除影视
     * @param id
     */
    @Override
    public void deleteMovieById(Integer id) {
        movieMapper.deleteById(id);
    }

    /**
     * 根据影视id批量删除影视
     * @param ids
     */
    @Transactional
    @Override
    public void deleteByIds(Integer[] ids) {
        if(ids!=null && ids.length >0){
            for (Integer id : ids) {
                movieMapper.deleteById(id);
            }
        }
    }

    /**
     * 根据导航id查询影视列表
     * @param cid
     * @return
     */
    @Override
    public List<Movie> findByCid(Integer cid) {
        QueryWrapper<Movie> wrapper = new QueryWrapper<>();
        wrapper.eq("cid",cid); // 查询条件where cid = ?
        return movieMapper.selectList(wrapper);
    }

    /**
     * 根据条件查询影视
     * @param page
     * @param query
     * @return
     */
    @Override
    public IPage<Movie> pageMovieByQuery(IPage<Movie> page, Movie query) {
        return movieMapper.pageMovieByQuery(page,query);
    }

    /**
     * 编辑影片信息
     * @param movie
     */
    @Override
    public void editMovie(Movie movie) {
        LambdaQueryWrapper<Movie> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Movie::getName,movie.getName());
        Movie dbMovie = movieMapper.selectOne(queryWrapper);
        // 如果查到的用户名存在且不是当前要编辑的记录，抛出异常
        if(ObjectUtil.isNotEmpty(dbMovie) && !Integer.valueOf(dbMovie.getId()).equals(dbMovie.getId())){
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        // 更新信息
        movieMapper.updateById(movie);
    }

    /**
     * 获取热门电影(分数最高的前十二)
     * @return
     */
    @Override
    public List<Movie> getPopularMovie() {
//        LambdaQueryWrapper<Movie> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(Movie::getScore).last("LIMIT 12");
//        List<Movie> list = movieMapper.selectList(queryWrapper);
        List<Movie> list = movieMapper.getPopularMovie();
        return list;
    }

    public List<Movie> getTopMoviesByCategory(Integer categoryId, int limit) {
        LambdaQueryWrapper<Movie> queryWrapper = new LambdaQueryWrapper<>();
        // 查询当前分类及其子分类的电影
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);

        // 获取子分类
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.eq(Category::getParentId, categoryId);
        List<Category> subCategories = categoryMapper.selectList(categoryWrapper);
        categoryIds.addAll(subCategories.stream().map(Category::getId).collect(Collectors.toList()));

        // 构建查询条件
        queryWrapper.in(Movie::getCid, categoryIds)
                .orderByDesc(Movie::getScore)
                .last("LIMIT " + limit);

        return movieMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Movie> pageTvShows(IPage<Movie> page, Movie query) {
        return movieMapper.pageTvShows(page, query);
    }

    @Override
    public IPage<Movie> pageMovieList(IPage<Movie> page, Integer categoryId, String release) {
        return movieMapper.pageMovieList(page, categoryId, release);
    }

    /**
     * 关键词查找影视
     * @param keyWord
     * @return
     */
//    @Override
//    public List<Movie> search(String keyWord) {
//        List<Movie> list = movieMapper.search(keyWord);
//        return list;
//    }

    /**
     * 前端分页查询
     * @param page
     * @param keyWord
     * @return
     */
    @Override
    public IPage<Movie> pageBySearch(IPage<Movie> page, String keyWord) {
        return movieMapper.pageBySearch(page,keyWord);
    }

    /**
     * 影视评分
     * @param movieId
     * @param userId
     * @param score
     * @return
     */
    @Override
    public Boolean rateMovie(Integer movieId, Integer userId, Integer score) {
        String key = "movie:rate"+movieId;
        if(redisTemplate.opsForSet().isMember(key,userId)){
            throw new ServiceException("您已经评分过了！");
        }

        // 更新电影评分
        Movie movie = movieMapper.selectById(movieId);
        if(movie == null){
            throw new ServiceException("电影不存在");
        }

        // 计算新的平均分
        double totalScore = (movie.getScore() != null ? movie.getScore() * (movie.getScoreCount() != null ? movie.getScoreCount() : 0) : 0) + score;
        int newCount = (movie.getScoreCount() != null ? movie.getScoreCount() : 0) + 1;
        double newScore = totalScore / newCount;

        // 更新数据
        movie.setScore(newScore);
        movie.setScoreCount(newCount);
        movieMapper.updateById(movie);

        // 记录用户评分
        redisTemplate.opsForSet().add(key,userId);
        return true;
    }

    @Override
    public Boolean hasUserRated(Integer movieId, Integer userId) {
        String key = "movie:rate"+movieId;
        return redisTemplate.opsForSet().isMember(key, userId);
    }

    @Override
    public List<Movie> getTopCollectedMovies(int limit) {
        return movieMapper.selectList(new LambdaQueryWrapper<Movie>()
                .orderByDesc(Movie::getCollectCount)
                .last("LIMIT " + limit));
    }


    @Override
    public List<Movie> getTopRatedMovies(int limit) {
        return movieMapper.selectList(new LambdaQueryWrapper<Movie>()
                .orderByDesc(Movie::getScore)
                .last("LIMIT " + limit));
    }

    @Override
    public Long movieCount() {
        return movieMapper.selectCount(null);
    }

    @Override
    public List<Movie> movieList() {
        return movieMapper.selectList(null);
    }
}
