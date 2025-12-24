package com.kfc.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kfc.backend.dto.AdminLoginDTO;
import com.kfc.backend.entity.AdminUser;
import com.kfc.backend.mapper.AdminUserMapper;
import com.kfc.backend.service.AdminUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Override
    public AdminUser login(AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();

        // 1. 根据用户名查询数据库
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername, username);
        AdminUser adminUser = this.getOne(queryWrapper);

        // 2. 校验账号是否存在
        if (adminUser == null) {
            throw new RuntimeException("登录失败：账号不存在");
        }

        // 3. 校验密码 (MD5加密后比对)
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(adminUser.getPassword())) {
            throw new RuntimeException("登录失败：密码错误");
        }

        // 4. (可选) 检查状态，如果有status字段的话
        // if (adminUser.getStatus() == 0) { throw ... }

        return adminUser;
    }
}