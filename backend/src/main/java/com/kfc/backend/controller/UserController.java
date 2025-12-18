package com.kfc.backend.controller;

import com.kfc.backend.entity.User;
import com.kfc.backend.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "C端顾客接口", description = "处理顾客登录、查身份")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "模拟微信登录 (根据ID获取用户信息)")
    @GetMapping("/login")
    public User login(@RequestParam Long userId) {
        // 真实开发这里会调用微信API换取openid，现在我们直接查数据库模拟
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user; // 返回的信息里包含 isVip，前端拿到后就能判断展示什么价格
    }
}