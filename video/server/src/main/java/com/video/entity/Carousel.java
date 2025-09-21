package com.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// 轮播图
@Data
@TableName("carousel")
public class Carousel {
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("movie_id")
    private Integer movieId;
    private String name;
    private String cover;
    private String director;
    private String actor;
}
