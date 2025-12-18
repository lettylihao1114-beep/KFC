package com.kfc.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kfc.backend.entity.OrderDetail;
import com.kfc.backend.entity.Orders;
import com.kfc.backend.mapper.OrderDetailMapper;
import com.kfc.backend.mapper.OrdersMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "订单管理", description = "下单、支付、历史订单、商家接单")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    // =========== 🧑 C端 顾客接口 ===========

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public String create(@RequestBody Orders orders) {
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(1); // 1:待付款
        ordersMapper.insert(orders);
        return "下单成功，订单号：" + orders.getId();
    }

    @Operation(summary = "模拟支付")
    @PostMapping("/pay")
    public String pay(@RequestParam Long orderId) {
        Orders orders = ordersMapper.selectById(orderId);
        if (orders != null) {
            orders.setStatus(2); // 改为待接单(已支付)
            orders.setCheckoutTime(LocalDateTime.now());
            ordersMapper.updateById(orders);
            return "支付成功";
        }
        return "订单不存在";
    }

    @Operation(summary = "查询用户历史订单列表")
    @GetMapping("/user/list")
    public List<Orders> userOrders(@RequestParam Long userId) {
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("order_time"); // 按时间倒序，最新的在上面
        return ordersMapper.selectList(wrapper);
    }

    // 👇👇👇 本次新增：顾客查看订单详情 (买了哪些汉堡) 👇👇👇
    @Operation(summary = "顾客端-查询订单详情")
    @GetMapping("/user/detail")
    public List<OrderDetail> userDetail(@RequestParam Long orderId) {
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        return orderDetailMapper.selectList(wrapper);
    }

    // =========== 👨‍🍳 移动端 店长/管理员接口 ===========

    @Operation(summary = "店长手机端-查订单列表")
    @GetMapping("/admin/list")
    public List<Orders> adminList(@RequestParam(required = false) Integer status) {
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        // 如果传了状态就按状态查，不传就查所有
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByAsc("order_time"); // 先下的单在上面
        return ordersMapper.selectList(wrapper);
    }

    @Operation(summary = "店长手机端-看订单详情")
    @GetMapping("/admin/detail")
    public List<OrderDetail> adminDetail(@RequestParam Long orderId) {
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        return orderDetailMapper.selectList(wrapper);
    }

    @Operation(summary = "店长手机端-接单/出餐")
    @PutMapping("/admin/status")
    public String adminUpdateStatus(@RequestParam Long orderId, @RequestParam Integer status) {
        Orders orders = ordersMapper.selectById(orderId);
        if (orders != null) {
            orders.setStatus(status);
            ordersMapper.updateById(orders);
            return "操作成功";
        }
        return "订单不存在";
    }
}