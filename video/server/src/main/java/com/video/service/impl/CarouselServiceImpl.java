package com.video.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.common.ResultCode;
import com.video.entity.Carousel;
import com.video.entity.Movie;
import com.video.exception.CustomException;
import com.video.mapper.CarouselMapper;
import com.video.mapper.MovieMapper;
import com.video.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;
    @Autowired
    private MovieMapper movieMapper;

    /**
     * 返回轮播图列表
     * @return
     */
    @Override
    public List<Carousel> list() {
        return carouselMapper.selectList(null);
    }

    /**
     * 查找轮播图信息
     * @param page
     * @param query
     * @return
     */
    @Override
    public IPage<Carousel> pageByQuery(IPage<Carousel> page, Carousel query) {
        return carouselMapper.pageByQuery(page,query);
    }

    /**
     * 新增轮播图
     * @param carousel
     */
    @Override
    public Carousel saveCarousel(Carousel carousel) {
        LambdaQueryWrapper<Movie> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Movie::getName,carousel.getName());
        Movie movie = movieMapper.selectOne(queryWrapper);
        if(movie!=null){
            carousel.setMovieId(movie.getId());
            carousel.setDirector(movie.getDirector());
            carousel.setActor(movie.getActor());
            carouselMapper.insert(carousel);
            return carousel;
        }else{
            throw new CustomException(ResultCode.MOVIE_NOT_EXIST);
        }
    }

    /**
     * 更新轮播图
     * @param carousel
     * @return
     */
    @Override
    public Carousel updateCarousel(Carousel carousel) {
        LambdaQueryWrapper<Movie> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Movie::getName,carousel.getName());
        Movie movie = movieMapper.selectOne(queryWrapper);
        if(movie!=null){
            carousel.setMovieId(movie.getId());
            carousel.setDirector(movie.getDirector());
            carousel.setActor(movie.getActor());
            carouselMapper.updateById(carousel);
            return carousel;
        }else{
            throw new CustomException(ResultCode.MOVIE_NOT_EXIST);
        }
    }

    /**
     * 根据id删除轮播图
     * @param id
     */
    @Override
    public void deleteCarouselById(Integer id) {
        carouselMapper.deleteById(id);
    }
}
