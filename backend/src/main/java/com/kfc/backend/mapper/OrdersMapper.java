package com.kfc.backend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kfc.backend.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {}