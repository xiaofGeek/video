package com.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("movie_tag")
public class MovieTag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer movieId;
    private Integer tagId;
}
