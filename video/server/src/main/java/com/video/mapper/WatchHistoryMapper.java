package com.video.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.entity.WatchHistory;

@Mapper
public interface WatchHistoryMapper extends BaseMapper<WatchHistory> {
    List<WatchHistory> getHistoryMovies(Integer userId);
}
