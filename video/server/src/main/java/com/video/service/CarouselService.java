package com.video.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Carousel;

import java.util.List;

public interface CarouselService {
    /**
     * 返回轮播图列表
     * @return
     */
    List<Carousel> list();

    /**
     * 查找轮播图信息
     * @param page
     * @param query
     * @return
     */
    IPage<Carousel> pageByQuery(IPage<Carousel> page, Carousel query);

    /**
     * 新增轮播图
     * @param carousel
     */
    Carousel saveCarousel(Carousel carousel);

    /**
     * 更新轮播图
     * @param carousel
     */
    Carousel updateCarousel(Carousel carousel);

    /**
     * 根据id删除轮播图
     * @param id
     */
    void deleteCarouselById(Integer id);
}
