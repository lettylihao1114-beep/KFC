package com.kfc.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kfc.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper 自动拥有 CRUD 能力
}