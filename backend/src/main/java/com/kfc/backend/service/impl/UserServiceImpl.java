package com.kfc.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kfc.backend.dto.UserLoginDTO;
import com.kfc.backend.dto.UserRegisterDTO;
import com.kfc.backend.entity.User;
import com.kfc.backend.mapper.UserMapper;
import com.kfc.backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 用户登录逻辑
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        // 1. 查数据库：根据用户名查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);

        // 2. 校验：用户是否存在
        if (user == null) {
            throw new RuntimeException("登录失败：账号不存在");
        }

        // 3. 校验：密码是否正确 (将前端传来的密码MD5加密后对比)
        String inputPasswordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!inputPasswordMd5.equals(user.getPassword())) {
            throw new RuntimeException("登录失败：密码错误");
        }

        // 4. 校验：账号状态 (假设 status=0 是禁用)
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账号已禁用");
        }

        return user;
    }

    /**
     * 用户注册逻辑
     */
    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();

        // 1. 查重：检查用户名是否已经存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User existUser = this.getOne(queryWrapper);

        if (existUser != null) {
            throw new RuntimeException("注册失败：用户名已存在");
        }

        // 2. 封装数据：将 DTO 转为 Entity
        User user = new User();
        user.setUsername(username);
        // 使用 MD5 加密密码
        user.setPassword(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()));

        // 设置一些默认值
        user.setPhone(userRegisterDTO.getPhone());
        user.setSex(userRegisterDTO.getSex());
        user.setStatus(1); // 默认启用

        // 3. 保存到数据库
        this.save(user);
    }
}