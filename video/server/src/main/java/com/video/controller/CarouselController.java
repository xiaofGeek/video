package com.video.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.Result;
import com.video.entity.Carousel;
import com.video.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carousel")
public class CarouselController {
    @Autowired
    private CarouselService carouselService;

    @GetMapping("/list")
    public Result list(){
        List<Carousel> list = carouselService.list();
        System.out.println("Carousel_list:"+list);
        return Result.success(list);
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") long pageNum,
                       @RequestParam(defaultValue = "5") long pageSize,
                       @RequestBody Carousel query) {
        IPage<Carousel> page = new Page<>(pageNum,pageSize);
        IPage<Carousel> list = carouselService.pageByQuery(page,query);
        return Result.success(list.getRecords(), list.getTotal());
    }

    @PostMapping("/save")
    public Result saveOrupdateCarousel(@RequestBody Carousel carousel){
        if(carousel.getId()==null){
            carouselService.saveCarousel(carousel);
        }else{
            carouselService.updateCarousel(carousel);
        }
        return Result.success(carousel);
    }

    @DeleteMapping("/{id}")
    public Result deleteMovieById(@PathVariable Integer id){
        carouselService.deleteCarouselById(id);
        return Result.success();
    }

    @PostMapping("/delBatch")
    public Result deleteBatchCarousel(@RequestBody List<Carousel> list){
        for (Carousel carousel : list) {
            carouselService.deleteCarouselById(carousel.getId());
        }
        return Result.success();
    }

}
