package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.Shop;
import com.kfc.backend.mapper.ShopMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "店铺管理", description = "首页店铺信息")
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopMapper shopMapper;

    @Operation(summary = "获取店铺状态信息")
    @GetMapping("/status")
    public Shop getShopStatus() {
        // 目前项目只是单店模式，直接返回 ID=1 的店铺即可
        // 以后如果是多店，这里可以改成根据经纬度返回最近的店
        return shopMapper.selectById(1L);
    }
}