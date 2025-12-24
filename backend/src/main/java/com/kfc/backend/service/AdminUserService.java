package com.kfc.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kfc.backend.dto.AdminLoginDTO;
import com.kfc.backend.entity.AdminUser;

public interface AdminUserService extends IService<AdminUser> {

    /**
     * 管理员登录
     */
    AdminUser login(AdminLoginDTO adminLoginDTO);
}