package com.video.service;


import com.video.entity.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 返回影视分类
     * @return
     */
    List<Category> list();

    /**
     * 获取分类树
     * @return
     */
    List<Category> tree();

    /**
     * 新增分类
     * @param category
     */
    void add(Category category);

    /**
     * 更新分类
     * @param category
     */
    void update(Category category);

    /**
     * 删除分类
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过id查询名称
     * @param cid
     * @return
     */
    String findNameById(Integer cid);

    Category findById(Integer id);

    /**
     * 通过id进行分类
     * @param id
     * @return
     */
    String categoryById(Integer id);
}
