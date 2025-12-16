package com.kfc.backend.controller;

import com.kfc.backend.entity.Order;
import com.kfc.backend.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    // 下单接口: POST http://localhost:8080/order/create
    @PostMapping("/create")
    public String createOrder(@RequestBody Order order) {
        // 1. 设置默认状态 (0=待支付)
        order.setStatus(0);

        // 2. 存入数据库 (这一步会自动生成订单ID)
        orderMapper.insert(order);

        // 3. 返回成功消息
        return "下单成功！您的订单号是: " + order.getId();
    }

    // 支付接口: POST http://localhost:8080/order/pay?orderId=xxxxx
    @PostMapping("/pay")
    public String payOrder(@RequestParam Long orderId) {
        // 1. 先去数据库把订单查出来
        Order order = orderMapper.selectById(orderId);

        if (order == null) {
            return "支付失败：订单不存在";
        }

        // 2. 修改状态为 1 (已支付)
        order.setStatus(1);

        // 3. 更新回数据库
        orderMapper.updateById(order);

        return "支付成功！您的餐点正在制作中...";
    }
}