package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.Product;
import com.kfc.backend.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "产品管理(后台)", description = "肯德基经理专用接口")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    // 1. 查询所有商品 (这是给小程序/顾客看的)
    @Operation(summary = "获取所有菜单")
    @GetMapping("/list")
    public List<Product> getList() {
        return productMapper.selectList(null);
    }

    // ---------------- 以下是新增的“后台管理”接口 ----------------

    // 2. 上架新品 (增)
    @Operation(summary = "上架新商品")
    // ✅ 删掉 token 参数，后端自动去 Header 里拿，不需要 Controller 操心
    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
        productMapper.insert(product);
        return "上架成功！新汉堡ID: " + product.getId();
    }

    // 3. 修改商品 (改) - 比如涨价了，或者改名字
    @Operation(summary = "修改商品信息")
    @PutMapping("/update")
    public String updateProduct(@RequestBody Product product) {
        productMapper.updateById(product);
        return "修改成功！";
    }

    // 4. 下架商品 (删)
    @Operation(summary = "下架/删除商品")
    @DeleteMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        productMapper.deleteById(id);
        return "删除成功！该商品已下架。";
    }
}