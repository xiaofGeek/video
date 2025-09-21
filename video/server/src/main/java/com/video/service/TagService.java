package com.video.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Tag;

import java.util.List;


public interface TagService {
    /**
     * 返回标签列表
     * @return
     */
    List<Tag> list();

    /**
     * 保存标签
     * @param tag
     */
    void save(Tag tag);

    /**
     * 更新标签
     * @param tag
     */
    void updateById(Tag tag);

    /**
     * 删除标签
     * @param id
     */
    void removeById(Integer id);

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    IPage<Tag> pageByQuery(IPage<Tag> page, Tag query);
}
