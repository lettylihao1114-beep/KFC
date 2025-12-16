package com.kfc.backend.controller;

import com.kfc.backend.entity.Product;
import com.kfc.backend.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product") // 1. 以后在这个控制器下的请求都以 /product 开头
public class ProductController {

    // 2. 核心关键：自动注入 Mapper
    // Spring Boot 会自动把 ProductMapper 的实现类做出来并塞到这里
    @Autowired
    private ProductMapper productMapper;

    // 3. 访问地址: http://localhost:8080/product/list
    @GetMapping("/list")
    public List<Product> list() {
        // selectList(null) 意味着：Select * from product (没有 where 条件)
        // MyBatis-Plus 会自动把查出来的 SQL 结果变成 Java 的 List<Product>
        return productMapper.selectList(null);
    }
}