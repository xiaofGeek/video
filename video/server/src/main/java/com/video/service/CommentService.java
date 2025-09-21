package com.video.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Comment;

import java.util.List;

public interface CommentService {
    /**
     * 返回指定电影id下的所有评论
     * @param movieId
     * @return
     */
    List<Comment> findAll(Integer movieId);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    Comment addComment(Comment comment);

    /**
     * 分页查询评论
     * @param page
     * @param query
     * @return
     */
    IPage<Comment> pageByQuery(IPage<Comment> page, Comment query);

    /**
     * 根据id删除评论
     * @param id
     */
    void deleteCommentById(Integer id);

    /**
     * 判断用户是否评论点赞或点踩
     * @param userId
     * @param commentId
     * @param isPrise
     */
    void togglePriseOrTread(Integer userId, Integer commentId, boolean isPrise);

    /**
     * 统计评论总数
     * @return
     */
    Long commentCount();
}
