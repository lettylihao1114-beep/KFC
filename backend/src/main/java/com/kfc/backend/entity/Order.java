package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("orders")
public class Order {

    // === 1. 防止前端精度丢失 (必加) ===
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    // === 2. 关联下单的用户 (必加) ===
    private Long userId;

    // === 3. 字段统一：实付金额 (对应数据库 amount 或 total_price) ===
    // 之前叫 totalPrice，为了配合 Controller 统一改为 amount
    private BigDecimal amount;

    // === 4. 核心字段：原价 (用于展示划线价格) ===
    private BigDecimal originalAmount;

    private String customerName; // 顾客名字 (保留)
    private Integer status;      // 0:未支付, 1:已支付, 2:已完成
    private LocalDateTime createTime; // 下单时间

    // ==========================================
    //       Getter 和 Setter (已自动生成)
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}