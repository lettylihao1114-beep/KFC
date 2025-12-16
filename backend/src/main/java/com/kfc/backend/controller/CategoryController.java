package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.Category;
import com.kfc.backend.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分类管理", description = "左侧菜单栏接口")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    @Operation(summary = "获取分类列表")
    @GetMapping("/list")
    public List<Category> list(@RequestParam(required = false) Integer type) {
        // 条件构造器：按 sort 字段排序，type 如果有传就查特定类型
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByAsc("sort").orderByDesc("id");

        return categoryMapper.selectList(queryWrapper);
    }
}