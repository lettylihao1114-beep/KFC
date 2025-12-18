package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("voucher")
public class Voucher {
    private Long id;
    private Long userId;
    private String title;
    private BigDecimal value;
    private Integer status; // 0:未使用 1:已使用

    // --- 手动 Getter/Setter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}