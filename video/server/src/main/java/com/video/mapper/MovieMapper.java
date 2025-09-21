package com.video.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Movie;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    IPage<Movie> pageMovieByQuery(IPage<Movie> page, Movie query);

    List<Movie> selectByCategory(@Param("categoryId")Integer categoryId);

    IPage<Movie> pageTvShows(IPage<Movie> page, @Param("query") Movie query);

    IPage<Movie> pageMovieList(IPage<Movie> page, @Param("categoryId") Integer categoryId, @Param("release") String release);

    List<Movie> getPopularMovie();

    List<Movie> search(String keyWord);

    IPage<Movie> pageBySearch(IPage<Movie> page, String keyWord);
}
