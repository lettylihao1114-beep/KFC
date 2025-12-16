package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("orders") // 对应数据库里的 orders 表
public class Order {
    private Long id;
    private String customerName; // 顾客名字
    private BigDecimal totalPrice; // 总金额
    private Integer status;      // 0:未支付, 1:已支付
    private LocalDateTime createTime; // 下单时间

    // ⚡️ 必做：在此处右键 -> 生成(Generate) -> Getter 和 Setter -> 全选 -> 确定

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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