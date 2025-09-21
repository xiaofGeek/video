package com.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("episode")
public class Episode {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("movie_id")
    private Integer movieId;
    
    @TableField("episode_number")
    private Integer episodeNumber;
    
    @TableField("play_id")
    private String playId;
    
    @TableField("name")
    private String name;
}