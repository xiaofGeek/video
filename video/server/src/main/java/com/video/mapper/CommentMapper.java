package com.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select * from comment where movieId=#{movieId} and commentId=#{commentId}")
    List<Comment> findByNewsIdAndCommentId(@Param("movieId") Integer movieId, @Param("commentId") Integer commentId);

    IPage<Comment> pageByQuery(IPage<Comment> page, Comment query);

    @Select("SELECT c.id, c.userId, c.movieId, c.commentId, c.movieName, " +
            "c.content, c.prise, c.tread, c.commentTime, " +
            "u.name as user_name, u.avatar " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.userId = u.id " +
            "WHERE c.id = #{commentId}")
    Comment getCommentDetail(@Param("commentId") Integer commentId);
}
