package com.kfc.backend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kfc.backend.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {}