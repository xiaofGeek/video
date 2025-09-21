package com.video.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Tag;
import com.video.mapper.TagMapper;
import com.video.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    /**
     * 返回标签列表
     * @return
     */
    @Override
    public List<Tag> list() {
        return tagMapper.selectList(null);
    }

    /**
     * 保存标签
      * @param tag
     */
    @Override
    public void save(Tag tag) {
        tagMapper.insert(tag);
    }

    /**
     * 更新标签
     * @param tag
     */
    @Override
    public void updateById(Tag tag) {
        tagMapper.updateById(tag);
    }

    /**
     * 删除标签
     * @param id
     */
    @Override
    public void removeById(Integer id) {
        tagMapper.deleteById(id);
    }

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    @Override
    public IPage<Tag> pageByQuery(IPage<Tag> page, Tag query) {
        return tagMapper.pageByQuery(page,query);
    }
}
