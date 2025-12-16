package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.AdminUser;
import com.kfc.backend.mapper.AdminUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "登录管理", description = "用于获取Token通行证")
@RestController
@RequestMapping("/auth")
public class LoginController {

    // 📖 小本本：用来存放所有合法的 Token (在内存里)
    public static Map<String, AdminUser> sessionMap = new HashMap<>();

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public String login(@RequestBody AdminUser loginUser) {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", loginUser.getUsername());
        wrapper.eq("password", loginUser.getPassword());

        AdminUser user = adminUserMapper.selectOne(wrapper);

        if (user == null) {
            return "登录失败：账号或密码错误！"; // 这里的文字其实是给前端看的
        }

        // 生成通行证
        String token = UUID.randomUUID().toString();

        // ✨ 关键一步：把通行证记在小本本上！
        sessionMap.put(token, user);

        return token; // 直接返回 Token，方便前端提取
    }
}