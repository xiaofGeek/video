package com.video.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.video.entity.Movie;
import com.video.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.entity.Category;
import com.video.mapper.CategoryMapper;
import com.video.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private MovieMapper movieMapper;

    @Override
    public List<Category> list() {
        List<Category> categories = categoryMapper.selectList(null);
        System.out.println("categories:"+categories);
        return categories;
    }

    @Override
    public void add(Category category) {
        // 检查名称是否重复
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName());

        if (categoryMapper.selectCount(wrapper) > 0) {
            throw new ServiceException("分类名称已存在");
        }

        // 如果是子分类，检查父分类是否存在
        if (category.getParentId() != null) {
            Category parent = categoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new ServiceException("父分类不存在");
            }
        }else{
            category.setParentId(0);
        }

        categoryMapper.insert(category);
    }

    @Override
    public List<Category> tree() {
        // 获取所有分类
        List<Category> allCategories = categoryMapper.selectList(null);
        System.out.println("所有分类数据: " + allCategories);  // 添加日志

        // 找出一级分类 - 这里的条件需要修改
        List<Category> rootCategories = allCategories.stream()
                .filter(category -> category.getParentId() == 0)  // 修改这里，parentId == 0 的才是父类
                .collect(Collectors.toList());
        System.out.println("父类数据: " + rootCategories);  // 添加日志

        // 找出子分类 - 这里的条件也需要修改
        Map<Integer, List<Category>> childrenMap = allCategories.stream()
                .filter(category -> category.getParentId() != 0)  // 修改这里，parentId != 0 的是子类
                .collect(Collectors.groupingBy(Category::getParentId));
        System.out.println("子类数据: " + childrenMap);  // 添加日志

        // 组装树形结构
        rootCategories.forEach(root -> {
            root.setChildren(childrenMap.getOrDefault(root.getId(), new ArrayList<>()));
        });

        return rootCategories;
    }

    @Override
    public void update(Category category) {
        try {
            // 检查分类是否存在
            Category exists = categoryMapper.selectById(category.getId());
            if (exists == null) {
                throw new ServiceException("分类不存在");
            }

            // 检查名称是否重复
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getName, category.getName())
                    .eq(category.getParentId() != null, Category::getParentId, category.getParentId())
                    .ne(Category::getId, category.getId());

            Long count = categoryMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new ServiceException("该分类名称已存在");
            }


            // 更新分类
            int rows = categoryMapper.updateById(category);
            if (rows <= 0) {
                throw new ServiceException("更新分类失败");
            }

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新分类失败", e);
            throw new ServiceException("系统错误，更新分类失败");
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            // 参数校验
            if (id == null) {
                throw new ServiceException("参数错误：ID不能为空");
            }

            // 检查分类是否存在
            Category category = categoryMapper.selectById(id);
            if (category == null) {
                throw new ServiceException("分类不存在");
            }

            // 检查是否有子分类
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getParentId, id);

            Long count = categoryMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new ServiceException("该分类下有子分类，不能直接删除");
            }

            // 删除分类
            int rows = categoryMapper.deleteById(id);
            if (rows <= 0) {
                throw new ServiceException("删除分类失败");
            }

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除分类失败", e);
            throw new ServiceException("系统错误，删除分类失败");
        }
    }

    /**
     * 通过id查询名称
     * @param cid
     * @return
     */
    @Override
    public String findNameById(Integer cid) {
        return categoryMapper.findNameById(cid);
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectById(id);
    }

    /**
     * 通过电影id获取其所属的父分类名称
     * @param movieId 电影ID
     * @return 父分类名称
     */
    @Override
    public String categoryById(Integer movieId) {
    // 1. 通过电影ID获取电影信息
    Movie movie = movieMapper.selectById(movieId);
    if (movie == null || movie.getCid() == null) {
        throw new ServiceException("电影不存在或未分类");
    }

    // 2. 获取电影关联的分类信息
    Category category = categoryMapper.selectById(movie.getCid());
    if (category == null) {
        throw new ServiceException("分类信息不存在");
    }

    // 3. 如果是子分类，获取父分类名称和子分类名称
    if (category.getParentId() != 0) {
        Category parentCategory = categoryMapper.selectById(category.getParentId());
        if (parentCategory == null) {
            throw new ServiceException("父分类信息不存在");
        }
        // 返回完整的分类路径
        return parentCategory.getName() + "/" + category.getName();
    }

    // 4. 如果是父分类直接返回名称
    return category.getName();
}
}
