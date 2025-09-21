package com.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("userId")
    private Integer userId;
    @TableField("movieId")
    private Integer movieId;
    @TableField("commentId")
    private Integer commentId;
    @TableField("movieName")
    private String movieName;
    private String content;
    private int prise;
    private int tread;
    @TableField("commentTime")
    private String commentTime;


    @Transient
    @TableField(exist = false)
    private String userName; // 用于展示评论人用户名
    @Transient
    @TableField(exist = false)
    private List<Comment> children; // 评论的子评论集合，用于前端渲染子评论
    @Transient
    @TableField(exist = false)
    private String avatar; // 用于展示评论人头像
    @Transient
    @TableField(exist = false)
    private String reply;
    @Transient
    @TableField(exist = false)
    private Boolean userPrise; // 用户是否点赞
    @Transient
    @TableField(exist = false)
    private Boolean userTread; // 用户是否点踩

    @TableField(exist = false)
    private Boolean hasLiked;    // 当前用户是否已点赞
    @TableField(exist = false)
    private Boolean hasDisliked; // 当前用户是否已点踩

}
