package com.kfc.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kfc.backend.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    // MyBatis-Plus 已经帮你写好了 insert 方法，这里留空就行
}