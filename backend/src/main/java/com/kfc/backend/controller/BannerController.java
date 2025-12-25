package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.Banner;
import com.kfc.backend.mapper.BannerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "轮播图管理", description = "小程序首页顶部的广告图")
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerMapper bannerMapper;

    @Operation(summary = "获取首页轮播图")
    @GetMapping("/list")
    public List<Banner> list() {
        try {
            // 只查询 status = 1 (启用) 的图片
            QueryWrapper<Banner> wrapper = new QueryWrapper<>();
            wrapper.eq("status", 1);
            return bannerMapper.selectList(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 返回空列表，防止报错
        }
    }

    // --- 后台管理接口 (需要Token) ---

    @Operation(summary = "添加轮播图")
    @PostMapping("/add")
    public String add(@RequestBody Banner banner) {
        bannerMapper.insert(banner);
        return "添加成功";
    }

    @Operation(summary = "删除轮播图")
    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id) {
        bannerMapper.deleteById(id);
        return "删除成功";
    }
}