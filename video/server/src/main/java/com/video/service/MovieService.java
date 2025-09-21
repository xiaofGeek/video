package com.video.service;


import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Movie;

public interface MovieService {
    /**
     * 查询影视列表
     * @param cid
     * @return
     */
    List<Movie> list(Integer cid);

    /**
     * 新增影视
     * @param movie
     */
    Movie saveMovie(Movie movie);

    /**
     * 更新影视
     * @param movie
     */
    Movie update(Movie movie);

    /**
     * 根据影视id查询影视详情
     * @param id
     * @return
     */
    Movie findById(Integer id);

    /**
     * 根据影视id删除影视
     * @param id
     */
    void deleteMovieById(Integer id);

    /**
     * 根据影视id批量删除影视
     * @param ids
     */
    void deleteByIds(Integer[] ids);

    /**
     * 根据导航id查询影视列表
     * @param cid
     * @return
     */
    List<Movie> findByCid(Integer cid);

    /**
     * 根据条件查询影视
     * @param page
     * @param query
     * @return
     */
    IPage<Movie> pageMovieByQuery(IPage<Movie> page, Movie query);

    /**
     * 编辑影片信息
     * @param movie
     */
    void editMovie(Movie movie);

    /**
     * 获取热门电影
     * @return
     */
    List<Movie> getPopularMovie();

    /**
     * 获取分类电影
     * @param categoryId
     * @param limit
     * @return
     */
    List<Movie> getTopMoviesByCategory(Integer categoryId, int limit);

    /**
     * 查询电视剧
     * @param page
     * @param query
     * @return
     */
    IPage<Movie> pageTvShows(IPage<Movie> page, Movie query);

    /**
     * 前端列表分页查询
     * @param page 分页参数
     * @param categoryId 分类ID
     * @return 分页结果
     */
    IPage<Movie> pageMovieList(IPage<Movie> page, Integer categoryId, String release);

    /**
     * 关键词查找影视
     * @param keyWord
     * @return
     */
//    List<Movie> search(String keyWord);

    /**
     * 前端分页查询
     * @param page
     * @param keyWord
     * @return
     */
    IPage<Movie> pageBySearch(IPage<Movie> page, String keyWord);

    /**
     * 影视评分
     * @param movieId
     * @param userId
     * @param score
     * @return
     */
    Boolean rateMovie(Integer movieId, Integer userId, Integer score);

    /**
     * 检查用户是否已评分
     * @param movieId
     * @param userId
     * @return
     */
    Boolean hasUserRated(Integer movieId, Integer userId);

    /**
     * 获取前10个最多收藏的电影
     * @return
     */
    List<Movie> getTopCollectedMovies(int limit);

    /**
     * 获取前10个评分最高的电影
     * @param limit
     * @return
     */
    List<Movie> getTopRatedMovies(int limit);

    /**
     * 统计影片总数
     * @return
     */
    Long movieCount();

    /**
     * 遍历影视
     * @return
     */
    List<Movie> movieList();
}
