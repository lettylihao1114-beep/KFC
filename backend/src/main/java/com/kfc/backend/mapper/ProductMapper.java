package com.kfc.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kfc.backend.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 这里什么都不用写，MyBatis-Plus 已经帮你写好了增删改查
}