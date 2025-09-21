package com.video.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.video.common.Result;
import com.video.exception.CustomException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.VodTemplate;
import com.video.entity.Category;
import com.video.entity.Movie;
import com.video.service.CategoryService;
import com.video.service.MovieService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelUtil;


@CrossOrigin
@RestController // 自动返回数据转换json格式
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private VodTemplate vodTemplate;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @param cid
     * @return
     */
    @GetMapping("/list")
    public Result list(@RequestParam(required = false) Integer cid) {
        List<Movie> movies = movieService.list(cid);
        return Result.success(movies);
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") long pageNum,
                       @RequestParam(defaultValue = "10") long pageSize,
                       @RequestBody Movie query) {
        IPage<Movie> page = new Page<>(pageNum,pageSize);
        IPage<Movie> list = movieService.pageMovieByQuery(page,query);
        return Result.success(list.getRecords(), list.getTotal());
    }

    /**
     * 更新/保存影视
     * @param movie
     * @return
     */
    @PostMapping("/save")
    public Result saveMovie(@RequestBody Movie movie){
        System.out.println("movieId:"+movie.getId());
        if(movie.getId()==null){
            movieService.saveMovie(movie);
        }else{
            movieService.update(movie);
        }
        return Result.success(movie);
    }

    /**
     * 根据id删除影视
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteMovieById(@PathVariable Integer id){
        movieService.deleteMovieById(id);
        return Result.success();
    }

    /**
     * 根据id批量删除影视
     * @param list
     * @return
     */
    @PostMapping("/delBatch")
    public Result deleteBatchMovie(@RequestBody List<Movie> list){
        for (Movie movie : list) {
            movieService.deleteMovieById(movie.getId());
        }
        return Result.success();
    }

    /**
     * 编辑影视信息
     * @param movie
     * @return
     */
    @PostMapping("/edit")
    public Result editMovie(@RequestBody Movie movie) {
        movieService.editMovie(movie);
        return Result.success();
    }

    @GetMapping("/playAuth")
    public Result getVideoPlayAuth(@RequestParam("playId") String playId) {
        try {
            // 调用阿里云 API 获取播放凭证
            String playAuth = vodTemplate.getVideoPlayAuth(playId).getPlayAuth();
            return Result.success(playAuth);
        } catch (Exception e) {
            return Result.error("获取播放凭证失败");
        }
    }

    /**
     * 根据id查询影视详情页
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result movieDetail(@RequestParam("id") Integer id){
        // 查询影视信息
        Movie movie = movieService.findById(id);
        return Result.success(movie);
    }

    /**
     * 电影热门排行榜
     * @return
     */
    @GetMapping("/getPopularMovie")
    public Result getPopularMovie(){
        // 查询影视信息
        List<Movie> list = movieService.getPopularMovie();
        return Result.success(list);
    }

    /**
     * 通过excel批量导入
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<Movie> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Movie.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            for (Movie info : infoList) {
                try {
                    movieService.saveMovie(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }

    /**
     * 影视分类
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result findCategoryById(@PathVariable Integer id) {
        Category category = categoryService.findById(id);  // 先查找当前分类
        String categoryPath = "";
        if (category != null) {
            if (category.getParentId() != null && category.getParentId() != 0) {
                // 如果有父分类，查找父分类
                Category parentCategory = categoryService.findById(category.getParentId());
                if (parentCategory != null) {
                    categoryPath = parentCategory.getName() + "/" + category.getName();
                }
            } else {
                categoryPath = category.getName();
            }
        }
        return Result.success(categoryPath);
    }

    /**
     * 批量导出到excel
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<Movie> all = movieService.movieList();
        List<Map<String, Object>> list = new ArrayList<>(all.size());
        if (CollectionUtil.isEmpty(all)) {
            throw new CustomException("-1", "未查询到数据");
        }
        for (Movie movie : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("影视名称", movie.getName());
            row.put("分类", categoryService.findNameById(movie.getCid()));
            row.put("简介", movie.getDescription());
            row.put("导演", movie.getDirector());
            row.put("主演", movie.getActor());
            row.put("关键词", movie.getKeyword());
            row.put("评分", movie.getScore());

            Date releaseDate = movie.getRelease();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(releaseDate);
            row.put("发布时间", date);
            list.add(row);
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=movies.xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    @GetMapping("/topByCategory/{categoryId}")
    public Result getTopMoviesByCategory(@PathVariable Integer categoryId) {
        List<Movie> movies = movieService.getTopMoviesByCategory(categoryId, 5); // 获取前5部电影
        return Result.success(movies);
    }

    @GetMapping("/listPage")
    public Result listPage(
            @RequestParam(required = false) Integer cid,
            @RequestParam(required = false) String release,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "12") Integer pageSize
    ) {
        try {
            IPage<Movie> page = new Page<>(pageNum, pageSize);
            IPage<Movie> result = movieService.pageMovieList(page, cid, release);
            return Result.success(result.getRecords(), result.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取影视列表失败");
        }
    }


    /**
     * 影视搜索
     * @param pageNum
     * @param pageSize
     * @param keyWord
     * @return
     */
    @GetMapping("/search")
    public Result pageBySearch(@RequestParam(defaultValue = "1") long pageNum,
                               @RequestParam(defaultValue = "10") long pageSize,
                               @RequestParam("keyWord") String keyWord) {
        IPage<Movie> page = new Page<>(pageNum,pageSize);
        IPage<Movie> list = movieService.pageBySearch(page,keyWord);
        return Result.success(list.getRecords(), list.getTotal());
    }

    /**
     * 影视评分
     * @param movieId
     * @param userId
     * @param score
     * @return
     */
    @PostMapping("/rate/{movieId}")
    public Result rate(@PathVariable Integer movieId,@RequestParam Integer userId,@RequestParam Integer score){
        return Result.success(movieService.rateMovie(movieId,userId,score));
    }

    /**
     * 检查用户是否已评分
     * @param movieId
     * @param userId
     * @return
     */
    @GetMapping("/checkRate/{id}")
    public Result checkUserRate(@PathVariable Integer movieId,@RequestParam Integer userId){
        return Result.success(movieService.hasUserRated(movieId,userId));
    }

    /**
     * 获取前10个最多收藏的电影
     * @return
     */
    @GetMapping("/topCollected")
    public Result getTopCollectedMovies() {
        List<Movie> movies = movieService.getTopCollectedMovies(10);
        return Result.success(movies);
    }

    /**
     * 获取前10个评分最高的电影
     * @return
     */
    @GetMapping("/topRated")
    public Result getTopRatedMovies() {
        List<Movie> movies = movieService.getTopRatedMovies(10);
        return Result.success(movies);
    }
}