package com.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarouselMapper extends BaseMapper<Carousel> {
    IPage<Carousel> pageByQuery(IPage<Carousel> page, Carousel query);
}
