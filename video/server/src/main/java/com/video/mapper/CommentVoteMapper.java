package com.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.entity.CommentVote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentVoteMapper extends BaseMapper<CommentVote> {
    @Select("select * from commentVote where userId=#{userId} and commentId=#{commentId}")
    CommentVote findByUserIdAndCommentId(Integer userId, Integer commentId);

}
