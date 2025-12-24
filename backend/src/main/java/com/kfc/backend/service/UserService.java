package com.kfc.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kfc.backend.dto.UserLoginDTO;
import com.kfc.backend.dto.UserRegisterDTO;
import com.kfc.backend.entity.User;

// 继承 IService 是为了使用 MyBatis-Plus 提供的通用 CRUD 方法
public interface UserService extends IService<User> {

    // 用户注册
    void register(UserRegisterDTO userRegisterDTO);

    // 用户登录
    User login(UserLoginDTO userLoginDTO);
}