package com.video.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.common.Result;
import com.video.common.config.JwtTokenUtils;
import com.video.entity.*;
import com.video.exception.CustomException;
import com.video.mapper.CommentMapper;
import com.video.mapper.CommentVoteMapper;
import com.video.mapper.UserMapper;
import com.video.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentVoteMapper commentVoteMapper;

    /**
     * 返回指定电影id下的所有评论
     * @param movieId
     * @return
     */
    @Override
    public List<Comment> findAll(Integer movieId) {
        // 登录验证
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        // 当前电影id下的所有评论
        List<Comment> list = commentMapper.findByNewsIdAndCommentId(movieId, 0);
        for (Comment comment : list) {
            setData(comment);
            // 当前评论下是否还有子评论
            List<Comment> children = commentMapper.findByNewsIdAndCommentId(movieId, comment.getId());
            if (CollectionUtil.isNotEmpty(children)) {
                for (Comment child : children) {
                    setData(child);
                }
                comment.setChildren(children);
            }
        }
        return list;
    }

    /**
     * 设置当前评论信息
     * @param comment
     */
    public void setData(Comment comment) {
        User user = userMapper.selectById(comment.getUserId());
        comment.setUserName(user.getName());
        comment.setAvatar(user.getAvatar());
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Override
    public Comment addComment(Comment comment) {
        System.out.println("||||||||||comment:"+comment);
        Comment addComment = new Comment();
        BeanUtils.copyProperties(comment,addComment);
        addComment.setCommentTime(DateUtil.now());
        commentMapper.insert(addComment);

        return addComment;
    }

    @Override
    public IPage<Comment> pageByQuery(IPage<Comment> page, Comment query) {
        return commentMapper.pageByQuery(page,query);
    }

    @Override
    public void deleteCommentById(Integer id) {
        commentMapper.deleteById(id);
    }

    @Override
    public void togglePriseOrTread(Integer userId, Integer commentId, boolean isPrise) {
        // 1. 查找用户对这条评论的所有投票记录
        CommentVote existingVote = commentVoteMapper.findByUserIdAndCommentId(userId, commentId);
        Comment comment = commentMapper.selectById(commentId);

        if (existingVote == null) {
            // 2. 如果没有投票记录，创建新记录
            CommentVote newVote = new CommentVote();
            newVote.setUserId(userId);
            newVote.setCommentId(commentId);
            newVote.setIsPrise(isPrise);
            commentVoteMapper.insert(newVote);

            // 更新评论的点赞/点踩数
            if (isPrise) {
                comment.setPrise(comment.getPrise() + 1);
            } else {
                comment.setTread(comment.getTread() + 1);
            }
        } else {
            if (existingVote.getIsPrise() == isPrise) {
                // 3. 如果已经点过赞/踩，则取消
                commentVoteMapper.deleteById(existingVote.getId());

                // 更新评论的点赞/点踩数
                if (isPrise) {
                    comment.setPrise(Math.max(0, comment.getPrise() - 1));
                } else {
                    comment.setTread(Math.max(0, comment.getTread() - 1));
                }
            } else {
                // 4. 如果之前点的是相反的，则切换
                existingVote.setIsPrise(isPrise);
                commentVoteMapper.updateById(existingVote);

                // 更新评论的点赞/点踩数
                if (isPrise) {
                    comment.setPrise(comment.getPrise() + 1);
                    comment.setTread(Math.max(0, comment.getTread() - 1));
                } else {
                    comment.setTread(comment.getTread() + 1);
                    comment.setPrise(Math.max(0, comment.getPrise() - 1));
                }
            }
        }

        // 更新评论的点赞/点踩数
        Comment updateComment = new Comment();
        updateComment.setId(commentId);
        updateComment.setPrise(comment.getPrise());
        updateComment.setTread(comment.getTread());
        commentMapper.updateById(updateComment);
    }

    @Override
    public Long commentCount() {
        return commentMapper.selectCount(null);
    }
}
