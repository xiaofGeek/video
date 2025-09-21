package com.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
    @Select("SELECT * FROM collect WHERE user_id = #{userId}")
    List<Collect> findMovieIdsByUserId(Integer userId);

    @Select("SELECT COUNT(*) FROM collect WHERE user_id = #{userId} AND movie_id = #{movieId}")
    int checkCollect(Integer userId, Integer movieId);
}