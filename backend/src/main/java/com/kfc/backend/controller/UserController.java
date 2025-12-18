package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.User;
import com.kfc.backend.entity.Voucher;
import com.kfc.backend.mapper.UserMapper;
import com.kfc.backend.mapper.VoucherMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "C端顾客接口", description = "处理顾客登录、查身份、查卡包")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VoucherMapper voucherMapper; // 👈 新增注入

    @Operation(summary = "模拟微信登录")
    @GetMapping("/login")
    public User login(@RequestParam Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    // 👇👇👇 新增接口：查询我的卡包 👇👇👇
    @Operation(summary = "查询我的卡包/优惠券")
    @GetMapping("/voucher/list")
    public List<Voucher> myVouchers(@RequestParam Long userId) {
        QueryWrapper<Voucher> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("status", 0); // 只查"未使用"的
        return voucherMapper.selectList(wrapper);
    }
}