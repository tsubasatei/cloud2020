package com.xt.springcloud.dao;

import com.xt.springcloud.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {

    // 1 新建订单
    void create(Order order);

    // 2 修改订单状态
    void update(@Param("userId") Long userId, @Param("status") Integer status);
}
