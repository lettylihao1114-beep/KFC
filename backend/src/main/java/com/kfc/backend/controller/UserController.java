package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.common.R;
import com.kfc.backend.entity.User;
import com.kfc.backend.entity.Voucher;
import com.kfc.backend.mapper.UserMapper;
import com.kfc.backend.mapper.VoucherMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal; // === 记得导入这个包 ===
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "C端顾客接口", description = "处理顾客登录、注册、查身份、查卡包、开通会员")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VoucherMapper voucherMapper;

    /**
     * 顾客登录接口 (适配前端 POST JSON)
     */
    @Operation(summary = "顾客账号密码登录")
    @PostMapping("/login")
    public R<User> login(@RequestBody User user) {
        log.info("顾客登录: {}", user.getUsername());

        // 1. 根据用户名查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        // 2. 校验账号
        if (dbUser == null) {
            return R.error("账号不存在");
        }

        // 3. 校验密码 (MD5加密比对)
        String inputPwd = user.getPassword();
        String md5Pwd = DigestUtils.md5DigestAsHex(inputPwd.getBytes(StandardCharsets.UTF_8));

        if (!dbUser.getPassword().equals(md5Pwd)) {
            return R.error("密码错误");
        }

        // 4. 校验状态
        if (dbUser.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        // 5. 检查会员过期逻辑
        if (dbUser.getIsVip() != null && dbUser.getIsVip() == 1 && dbUser.getVipExpireTime() != null) {
            if (LocalDateTime.now().isAfter(dbUser.getVipExpireTime())) {
                dbUser.setIsVip(0); // 过期变回普通用户
                userMapper.updateById(dbUser);
            }
        }

        // 6. 返回用户信息
        return R.success(dbUser);
    }

    /**
     * 新增：顾客注册接口
     */
    @Operation(summary = "顾客注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody User user) {
        log.info("新用户注册: {}", user.getUsername());

        // 1. 检查账号是否重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User exists = userMapper.selectOne(queryWrapper);

        if (exists != null) {
            return R.error("该账号已存在");
        }

        // 2. 密码加密
        if (user.getPassword() != null) {
            String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
            user.setPassword(md5Pwd);
        }

        // 3. 补全默认信息
        user.setIsVip(0);
        user.setStatus(1);

        // === 修复点：double 0.0 改为 BigDecimal.ZERO ===
        user.setBalance(BigDecimal.ZERO);

        if (user.getNickname() == null) {
            user.setNickname("用户" + user.getUsername());
        }

        // 4. 保存
        userMapper.insert(user);

        return R.success("注册成功");
    }

    /**
     * 查询卡包
     */
    @Operation(summary = "查询我的卡包/优惠券")
    @GetMapping("/voucher/list")
    public R<List<Voucher>> myVouchers(@RequestParam Long userId) {
        QueryWrapper<Voucher> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("status", 0); // 只查"未使用"的
        List<Voucher> list = voucherMapper.selectList(wrapper);
        return R.success(list);
    }

    /**
     * 开通会员
     */
    @Operation(summary = "开通/续费大神卡")
    @PostMapping("/buyVip")
    public R<User> buyVip(@RequestParam Long userId, @RequestParam Integer type) {
        User user = userMapper.selectById(userId);
        if (user == null) return R.error("用户不存在");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newExpireTime;

        // 逻辑：如果已经是VIP且没过期，就在原过期时间上顺延；否则从现在开始算
        if (user.getIsVip() != null && user.getIsVip() == 1 && user.getVipExpireTime() != null && user.getVipExpireTime().isAfter(now)) {
            newExpireTime = user.getVipExpireTime();
        } else {
            newExpireTime = now;
        }

        // 根据类型加时间
        if (type == 1) {
            newExpireTime = newExpireTime.plusDays(30); // 月卡
        } else if (type == 2) {
            newExpireTime = newExpireTime.plusDays(90); // 季卡
        } else {
            return R.error("未知的卡类型");
        }

        // 更新数据库
        user.setIsVip(1);
        user.setVipExpireTime(newExpireTime);
        userMapper.updateById(user);

        return R.success(user);
    }
}