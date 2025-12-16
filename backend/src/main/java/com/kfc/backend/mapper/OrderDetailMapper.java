package com.kfc.backend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kfc.backend.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {}