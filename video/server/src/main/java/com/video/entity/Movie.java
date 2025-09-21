package com.video.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.annotation.Alias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//影视
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("movie")
public class Movie{

    @Alias("影视Id")
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    private String name;//影视名
    private Integer cid;//所属栏目
    private String description;//描述
    private String keyword;//搜索关键字
    private String director;//导演
    private String actor;//演员
    private String image;//缩略图
    private Double score;//评分
    private Integer scoreCount; // 评分人数
    private String playId;//播放id
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @TableField(value = "`release`")
    private Date release; // 发布时间
    @TableField(exist = false)//不存储到数据库,仅仅用于前台数据展示
    private String playAuth;//播放秘钥

    @TableField("collect_count")
    private Integer collectCount; // 收藏数量
    @TableField(exist = false)
    private Boolean isCollected; // 当前用户是否已收藏
    // 新增电视剧相关字段
    @TableField("totalEpisodes")
    private Integer totalEpisodes;    // 总集数，电影就是null或1
    @TableField("currentEpisodes")
    private Integer currentEpisodes;  // 当前集数，电影就是null或1
}
