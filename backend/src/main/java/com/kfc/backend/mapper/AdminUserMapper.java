package com.kfc.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kfc.backend.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}