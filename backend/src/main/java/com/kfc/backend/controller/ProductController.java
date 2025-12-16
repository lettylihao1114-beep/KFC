package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.Product;
import com.kfc.backend.entity.ProductFlavor;
import com.kfc.backend.mapper.ProductFlavorMapper;
import com.kfc.backend.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "产品管理(后台+小程序)", description = "包含分类查询和规格选择")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductFlavorMapper productFlavorMapper;

    // 1. 查询所有商品 (升级版：支持按 categoryId 过滤)
    @Operation(summary = "获取菜单(支持按分类查)")
    @GetMapping("/list")
    public List<Product> getList(@RequestParam(required = false) Long categoryId) {
        // 1. 构造查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId); // 只查这个分类下的
        }
        queryWrapper.eq("status", 1); // 只查"起售"状态的
        queryWrapper.orderByAsc("price"); // 按价格排序

        List<Product> products = productMapper.selectList(queryWrapper);

        // 2. (进阶) 填充口味数据 (如：["可乐","雪碧"])
        // 前端点"选规格"时需要用到 flavors 字段
        for (Product product : products) {
            QueryWrapper<ProductFlavor> flavorWrapper = new QueryWrapper<>();
            flavorWrapper.eq("product_id", product.getId());
            List<ProductFlavor> flavors = productFlavorMapper.selectList(flavorWrapper);
            product.setFlavors(flavors);
        }

        return products;
    }

    // --- 后台管理接口 (保持之前的逻辑，稍微适配新字段) ---

    @Operation(summary = "上架新商品(含规格)")
    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
        // 1. 存基本信息
        productMapper.insert(product);

        // 2. 存口味信息 (如果有)
        Long productId = product.getId();
        List<ProductFlavor> flavors = product.getFlavors();
        if (flavors != null) {
            for (ProductFlavor flavor : flavors) {
                flavor.setProductId(productId);
                productFlavorMapper.insert(flavor);
            }
        }
        return "上架成功！ID: " + productId;
    }

    @Operation(summary = "修改商品")
    @PutMapping("/update")
    public String updateProduct(@RequestBody Product product) {
        productMapper.updateById(product);
        return "修改成功！";
    }

    @Operation(summary = "下架/删除商品")
    @DeleteMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        productMapper.deleteById(id);
        return "已下架/删除";
    }
}