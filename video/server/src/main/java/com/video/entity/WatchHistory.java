package com.video.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@TableName("watch_history")
public class WatchHistory {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;
    @TableField("movie_id")
    private Integer movieId;
    @TableField("movie_name")
    private String movieName;
    @TableField("last_episode")
    private Integer lastEpisode;
    @TableField("watch_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date watchTime;
}